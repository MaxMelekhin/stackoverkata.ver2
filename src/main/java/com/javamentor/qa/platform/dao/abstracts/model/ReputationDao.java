package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;

import java.util.Optional;

public interface ReputationDao extends ReadWriteDao<Reputation, Long> {

    Optional<Reputation> getReputationByAnswerIdAndUserId(Long senderId, Long answerId);
    Optional<Reputation> reputationExists (User sender, Long answerId);
}
