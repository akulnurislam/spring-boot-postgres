package com.akul.dsp.service.impl;

import com.akul.dsp.dto.LoginDTO;
import com.akul.dsp.exception.UnauthorizedException;
import com.akul.dsp.model.User;
import com.akul.dsp.service.LoginService;
import com.akul.dsp.service.UserService;
import com.akul.dsp.util.JWT;
import com.akul.dsp.util.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {
    private final UserService userService;
    private final JWT jwt;

    @Override
    public String generateToken(@NonNull LoginDTO loginDTO) {
        User user = userService.findOneByPhoneNumber(loginDTO.getPhoneNumber());

        if (!Password.verify(loginDTO.getPassword(), user.getSalt(), user.getPassword())) {
            throw new UnauthorizedException();
        }

        return jwt.generate(user.getPhoneNumber());
    }
}
