package com.example.demo.repository;


import com.example.demo.entity.VerifiedToken;
import org.springframework.data.repository.CrudRepository;

public interface VerifiedTokenRepository extends CrudRepository<VerifiedToken, String> {
    VerifiedToken findByVerifiedToken(String VerificationToken);
}
