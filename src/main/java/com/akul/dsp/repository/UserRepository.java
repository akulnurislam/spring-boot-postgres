package com.akul.dsp.repository;

import com.akul.dsp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByPhoneNumber(String phoneNumber);
}
