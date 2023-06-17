package com.javamentor.qa.platform.converters;

import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public abstract class UserConverter {
    @Mapping(source = "user", target = "firstName", qualifiedByName = "fullToFirstName")
    @Mapping(source = "user", target = "lastName", qualifiedByName = "fullToLastName")
    public abstract UserRegistrationDto userToUserRegistrationDto (User user);

    @Mapping(source = "userRegistrationDto", target = "fullName", qualifiedByName = "firstLastToFullName")
    @Mapping(source = "defaultRole", target = "role")
    public abstract User userRegistrationDtoToUser (UserRegistrationDto userRegistrationDto, Role defaultRole);

    @Named("fullToFirstName")
    public String fullToFirstName(User user){
        String[] fullName = user.getFullName().split(" ");
        if (fullName[0].isEmpty()) {
            return "";
        }
        return fullName[0];
    }

    @Named("fullToLastName")
    public String fullToLastName(User user){
        String[] fullName = user.getFullName().split(" ");
        if (fullName[1].isEmpty()) {
            return "";
        }
        return fullName[1];
    }

    @Named("firstLastToFullName")
    public String firstLastToFullName(UserRegistrationDto userRegistrationDto){
        return userRegistrationDto.getFirstName() + " " + userRegistrationDto.getLastName();
    }
}
