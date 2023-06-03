package com.capstone.backend.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.capstone.backend.constant.PostStatuses;
import com.capstone.backend.dto.postPayment.PostPaymentRequest;
import com.capstone.backend.dto.postPayment.PostPaymentResponse;
import com.capstone.backend.exception.DifferentTypeException;
import com.capstone.backend.exception.InsufficientBalanceException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.exception.UnqualifiedException;
import com.capstone.backend.mapper.PostPaymentMapper;
import com.capstone.backend.model.PackagePrice;
import com.capstone.backend.model.Post;
import com.capstone.backend.model.PostPayment;
import com.capstone.backend.model.User;
import com.capstone.backend.repository.PostPaymentRepository;
import com.capstone.backend.service.iservice.IPackagePriceService;
import com.capstone.backend.service.iservice.IPostPaymentService;
import com.capstone.backend.service.iservice.IPostService;
import com.capstone.backend.service.iservice.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostPaymentService implements IPostPaymentService {

  private final PostPaymentRepository postPaymentRepository;

  private final IPostService postService;

  private final IUserService userService;

  private final IPackagePriceService packagePriceService;

  private final PostPaymentMapper postPaymentMapper;

  private final MessageSource messageSource;

  @Override
  public PostPayment findById(long id) throws ResourceNotFoundException {
    return postPaymentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));
  }

  @Override
  public List<PostPaymentResponse> getPostPayments() {
    return postPaymentRepository.findAll()
        .stream()
        .filter(payment -> !payment.isDeleted())
        .sorted(Comparator.comparingLong(PostPayment::getId))
        .map(postPaymentMapper)
        .collect(Collectors.toList());
  }

  @Override
  public List<PostPaymentResponse> getPersonalPostPayments(String email) {
    return postPaymentRepository.findAll()
        .stream()
        .filter(payment -> !payment.isDeleted() && payment.getPost().getUser().getEmail().equals(email))
        .sorted(Comparator.comparingLong(PostPayment::getId))
        .map(postPaymentMapper)
        .collect(Collectors.toList());
  }

  @Override
  public PostPaymentResponse getPostPayment(long id) throws ResourceNotFoundException {
    return postPaymentMapper.apply(findById(id));
  }

  @Override
  public PostPaymentResponse purchase(String email, PostPaymentRequest request)
      throws ResourceNotFoundException,
      InsufficientBalanceException,
      DifferentTypeException,
      UnqualifiedException {
    Post post = postService.findById(request.postId());
    PackagePrice packagePrice = packagePriceService.findById(request.packagePriceId());
    User user = userService.findByEmail(email);
    LocalDateTime now = LocalDateTime.now();
    long numberOfDays = request.numberPackage() * packagePrice.getNumberOfDays();

    long totalAmount = request.numberPackage() * packagePrice.getPrice();
    if (user.getBalance() < totalAmount) {
      throw new InsufficientBalanceException(
          messageSource.getMessage("error.insufficient-balance", null, Locale.getDefault()));
    }

    if (post.getStatus().equals(PostStatuses.PUBLIC) && post.getExpiredAt().isAfter(now)) {
      // Gia hạn tin
      if (!post.getType().equals(packagePrice.getType())) {
        throw new DifferentTypeException(messageSource.getMessage("error.different-type", null, Locale.getDefault()));
      }
      post.setExpiredAt(post.getExpiredAt().plusDays(numberOfDays));
    } else if (post.getStatus().equals(PostStatuses.UNPAID)
        || (post.getStatus().equals(PostStatuses.PUBLIC) && post.getExpiredAt().isBefore(now))) {
      post.setStatus(PostStatuses.PUBLIC);
      post.setType(packagePrice.getType());
      post.setPaidAt(now);
      post.setExpiredAt(now.plusDays(numberOfDays));
    } else {
      throw new UnqualifiedException(messageSource
          .getMessage("error.unqualified", null, Locale.getDefault()));
    }

    user.setBalance(user.getBalance() - totalAmount);
    userService.save(user);

    postService.save(post);

    PostPayment payment = new PostPayment(request.numberPackage(), post, packagePrice);
    return postPaymentMapper.apply(postPaymentRepository.save(payment));
  }

  @Override
  public void deletePostPayment(long id) throws ResourceNotFoundException {
    PostPayment payment = findById(id);
    if (payment.isDeleted()) {
      throw new ResourceNotFoundException(
          messageSource.getMessage("error.resource-not-found", null, Locale.getDefault()));
    }

    payment.setDeleted(true);
    postPaymentRepository.save(payment);
  }
}
