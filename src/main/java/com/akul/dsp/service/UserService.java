package com.akul.dsp.service;

import com.akul.dsp.model.User;
import org.springframework.lang.NonNull;

public interface UserService {
    @NonNull
    User findOneByPhoneNumber(String phoneNumber);

    void create(User user);

    @NonNull
    User updateByPhoneNumber(String phoneNumber, User user);
}
