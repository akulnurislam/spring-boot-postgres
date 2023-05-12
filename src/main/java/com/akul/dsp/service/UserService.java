package com.akul.dsp.service;

import com.akul.dsp.dto.NameDTO;
import com.akul.dsp.dto.RequestDTO;
import com.akul.dsp.model.User;
import org.springframework.lang.NonNull;

public interface UserService {
    @NonNull
    User findOneByPhoneNumber(String phoneNumber);

    void create(RequestDTO data);

    @NonNull
    User updateByPhoneNumber(String phoneNumber, NameDTO data);
}
