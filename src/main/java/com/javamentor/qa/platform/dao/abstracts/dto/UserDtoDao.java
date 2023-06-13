package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;

import com.javamentor.qa.platform.models.entity.user.User;
import java.util.Optional;

public interface UserDtoDao {
    Optional<UserDto> getById(Long id);
    UserDto convertToDto(User user);
}
