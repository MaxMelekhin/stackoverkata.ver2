package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;

import java.util.Optional;

public interface VoteAnswerDao extends ReadWriteDao<VoteAnswer, Long> {

    Long getVoteAnswerAmount (Long answerId);

    Optional<VoteAnswer> voteAnswerExists (Long answerId, Long senderId);

    Long getAllTheVotesForThisAnswer(Long answer);

    Optional<VoteAnswer> getVoteAnswerByAnswerIdAndUserId(Long answerId, Long userId);
}
