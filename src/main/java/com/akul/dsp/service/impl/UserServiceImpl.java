package com.akul.dsp.service.impl;

import com.akul.dsp.dto.NameDTO;
import com.akul.dsp.dto.RequestDTO;
import com.akul.dsp.exception.NotFoundException;
import com.akul.dsp.model.User;
import com.akul.dsp.repository.UserRepository;
import com.akul.dsp.service.UserService;
import com.akul.dsp.util.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @NonNull
    @Override
    public User findOneByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException(format("user with phone number: %s not found", phoneNumber)));
    }

    @Override
    public void create(@NonNull RequestDTO data) {
        String salt = Password.salt();
        String hashed = Password.hash(data.getPassword(), salt);

        User user = new User();
        user.setPhoneNumber(data.getPhoneNumber());
        user.setName(data.getName());
        user.setPassword(hashed);
        user.setSalt(salt);
        userRepository.save(user);
    }

    @NonNull
    @Override
    public User updateByPhoneNumber(String phoneNumber, @NonNull NameDTO data) {
        User user = findOneByPhoneNumber(phoneNumber);
        user.setName(data.getName());

        return userRepository.save(user);
    }
}
