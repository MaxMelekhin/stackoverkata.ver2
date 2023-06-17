package com.javamentor.qa.platform.service.impl.util;

import com.javamentor.qa.platform.exception.EncodeDecodeException;
import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.service.abstracts.util.EncodeDecodeUserRegistrationDtoService;
import org.apache.commons.codec.binary.Base32;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EncodeDecodeUserRegistrationDtoServiceImp implements EncodeDecodeUserRegistrationDtoService {
    private Map<String, String> emailUserRegistration = new HashMap<>();
    private final Base32 base32 = new Base32();
    private final PasswordEncoder passwordEncoder;

    public EncodeDecodeUserRegistrationDtoServiceImp(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(UserRegistrationDto userRegistrationDto) {
        String passwordEncodered = passwordEncoder.encode(userRegistrationDto.getPassword());
        userRegistrationDto.setPassword(passwordEncodered);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(userRegistrationDto);
            oos.flush();
            oos.close();
            bos.close();
            final byte[] byteArray = bos.toByteArray();
            return new String(base32.encode(byteArray), StandardCharsets.UTF_8);
        } catch (IOException e){
            e.printStackTrace();
            throw new EncodeDecodeException("Exception encode user for unic URL");
        }
    }

    @Override
    public UserRegistrationDto decode(String code) {
        try {
            UserRegistrationDto userRegistrationDto = null;
            if (code != null && !code.isEmpty()) {
                final byte[] bytes = base32.decode(code);
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis);
                userRegistrationDto = (UserRegistrationDto) ois.readObject();
                ois.close();
                bis.close();
            }
            return userRegistrationDto;
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            throw new EncodeDecodeException("Exception decode user for persist");
        }
    }
}
