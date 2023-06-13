package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.abstracts.repository.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.impl.repository.PageDtoServiceImpl;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserDtoServiceImpl extends PageDtoServiceImpl<User, UserDto> implements UserDtoService {

    private final UserDtoDao userDtoDao;

    public UserDtoServiceImpl(UserDtoDao userDtoDao, PageDtoDao<User, UserDto> pageDtoDao) {
        super(pageDtoDao);
        this.userDtoDao = userDtoDao;
    }

    public Optional<UserDto> getUserDtoById(Long id) {
        return userDtoDao.getById(id);
    }

    @Override
    public UserDto convertToDto(User entity) {
        return userDtoDao.convertToDto(entity);
    }
}

