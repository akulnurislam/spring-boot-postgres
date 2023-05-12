package com.akul.dsp.service;

import com.akul.dsp.model.User;

public interface UserService {
    User findOneByPhoneNumber(String phoneNumber);

    void create(User user);

    User updateByPhoneNumber(String phoneNumber, User user);
}
