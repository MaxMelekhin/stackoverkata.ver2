package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.impl.repository.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import javax.persistence.Query;
import java.util.Optional;


@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void markAnswerAsDeleted(Long id) {
        entityManager.createQuery("""
                UPDATE Answer a SET a.isDeleted = true WHERE a.id = :id
                """)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Optional<Answer> getAnswerById(Long id, Long userId) {
        return SingleResultUtil.getSingleResultOrNull((Query) entityManager.createQuery("""
        from Answer a where a.id =: id and a.user.id !=: userId
        """, Answer.class)
                .setParameter("id", id)
                .setParameter("userId", userId));
    }

    @Override
    @Transactional
    public Optional<Answer> getByIdAndChecked (Long answerId, Long senderId) {
        return SingleResultUtil.getSingleResultOrNull(
                entityManager.createQuery("""
                    from Answer a 
                    where a.id = :answerId
                    and a.user.id != :senderId
                    """, Answer.class)
                .setParameter("answerId", answerId)
                .setParameter("senderId", senderId));
    }



}