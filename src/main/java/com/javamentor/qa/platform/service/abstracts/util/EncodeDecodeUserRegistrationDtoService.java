package com.javamentor.qa.platform.service.abstracts.util;

import com.javamentor.qa.platform.models.dto.UserRegistrationDto;

import java.io.IOException;

public interface EncodeDecodeUserRegistrationDtoService {
    public String encode(UserRegistrationDto userRegistrationDto) throws IOException;
    public UserRegistrationDto decode(String code) throws ClassNotFoundException, IOException;
}
