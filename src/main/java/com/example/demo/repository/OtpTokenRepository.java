package com.example.demo.repository;

import com.example.demo.entity.OtpToken;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpTokenRepository extends CrudRepository<OtpToken, String> {
  OtpToken findByOtpToken(String otpToken);
//    User getUserByConfirmationToken(String confirmationToken);
}
