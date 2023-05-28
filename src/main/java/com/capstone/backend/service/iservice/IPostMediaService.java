package com.capstone.backend.service.iservice;

import org.springframework.web.multipart.MultipartFile;

import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.PostMedia;

public interface IPostMediaService {

  PostMedia save(PostMedia media, MultipartFile file);

  PostMedia findById(long id) throws ResourceNotFoundException;

}
