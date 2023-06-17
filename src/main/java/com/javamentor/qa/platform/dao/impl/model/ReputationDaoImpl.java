package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.impl.repository.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation, Long> implements ReputationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<Reputation> reputationExists (User sender, Long answerId)  {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("""
                        select r
                        from Reputation r
                        where r.sender = :sender
                        and r.answer.id = :answerId
                        """, Reputation.class)
                .setParameter("sender", sender)
                .setParameter("answerId", answerId));
    }

    @Override
    public Optional<Reputation> getReputationByAnswerIdAndUserId(Long senderId, Long answerId) {

        return SingleResultUtil.getSingleResultOrNull(
                entityManager.createQuery("""
                        from Reputation r 
                        where r.answer.id = : answerId 
                        and r.sender.id = : senderId 
                        """, Reputation.class)
                        .setParameter("answerId", answerId)
                        .setParameter("senderId", senderId));
    }
}
