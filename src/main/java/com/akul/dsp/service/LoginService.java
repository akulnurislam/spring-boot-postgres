package com.akul.dsp.service;

import com.akul.dsp.dto.LoginDTO;

public interface LoginService {
    String generateToken(LoginDTO loginDTO);
}
