package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.repository.PageDtoService;
import java.util.Optional;

public interface UserDtoService extends PageDtoService<User, UserDto> {
    Optional<UserDto> getUserDtoById(Long id);


}
