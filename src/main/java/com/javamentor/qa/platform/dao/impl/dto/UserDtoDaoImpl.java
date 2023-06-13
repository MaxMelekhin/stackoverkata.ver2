package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.impl.repository.PageDtoDaoImpl;
import com.javamentor.qa.platform.dao.impl.repository.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public class UserDtoDaoImpl extends PageDtoDaoImpl<User, UserDto> implements UserDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<UserDto> getById(Long id) {
        TypedQuery<UserDto> query = entityManager.createQuery("""
                select new com.javamentor.qa.platform.models.dto.UserDto (
                    u.id,
                    u.email,
                    u.fullName,
                    u.imageLink as linkImage,
                    u.city,
                    COALESCE((select sum(r.count) from Reputation r where r.author.id = u.id), 0) as reputation,
                    u.persistDateTime as registrationDate,
                    COALESCE((select count(*) from VoteQuestion v where v.user.id = u.id), 0) +
                    COALESCE((select count(*) from VoteAnswer v where v.user.id = u.id), 0) as votes
                )
                from User u
                where u.id = :id
                """, UserDto.class);
        query.setParameter("id", id);
        try {
            UserDto userDto = query.getSingleResult();
            userDto.setListTop3TagDto(new ArrayList<>());      //<------------------заглушка
            return Optional.of(userDto);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public UserDto convertToDto(User user) {
        UserDto userDto = getById(user.getId()).get();
        return userDto;
    }
}