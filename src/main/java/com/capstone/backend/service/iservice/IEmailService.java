package com.capstone.backend.service.iservice;

public interface IEmailService {
  void sendConfirm(String to, String username, String link);

  void sendReset(String to, String username, String link);
}
