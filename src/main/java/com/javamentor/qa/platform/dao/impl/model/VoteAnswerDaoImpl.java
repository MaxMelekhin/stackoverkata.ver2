package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.dao.impl.repository.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long getVoteAnswerAmount (Long answerId) {

        return entityManager.createQuery("""
            select count(va.id)
            from VoteAnswer va
            where va.answer.id = :answerId
            """, Long.class)
                .setParameter("answerId", answerId)
                .getSingleResult();
    }

    @Override
    @Transactional
    public Optional<VoteAnswer> voteAnswerExists (Long answerId, Long senderId) {
        return SingleResultUtil.getSingleResultOrNull(
                entityManager.createQuery("""
                    from VoteAnswer va
                    where va.answer.id = :answerId
                    and va.user.id = :senderId
                    and va.voteType = 'DOWN'
                    """, VoteAnswer.class)
                .setParameter("answerId", answerId)
                .setParameter("senderId", senderId));
    }
    @Override
    public Long getAllTheVotesForThisAnswer(Long answerId) {

        return entityManager.createQuery("""
                        select count(v.id) from VoteAnswer v where v.answer.id = : answerId
                        """, Long.class)
                .setParameter("answerId", answerId)
                .getSingleResult();
    }

    @Override
    public Optional<VoteAnswer> getVoteAnswerByAnswerIdAndUserId(Long answerId, Long userId) {

        return SingleResultUtil.getSingleResultOrNull(
                entityManager.createQuery("""
                        from VoteAnswer v 
                        where v.answer.id = : answerId 
                        and v.user.id = : userId 
                        and v.voteType = 'UP' 
                        """, VoteAnswer.class)
                        .setParameter("answerId", answerId)
                        .setParameter("userId", userId));
    }
}




