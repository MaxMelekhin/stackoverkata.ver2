package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;

import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReputationServiceImpl extends ReadWriteServiceImpl<Reputation, Long> implements ReputationService {
    private final ReputationDao reputationDao;
    private final AnswerDao answerDao;
    private static final int DOWN_VOTE_POINTS = - 5;

    public ReputationServiceImpl(ReadWriteDao<Reputation, Long> readWriteDao,
                                 ReputationDao reputationDao, AnswerDao answerDao)
    {

        super(readWriteDao);
        this.reputationDao = reputationDao;
        this.answerDao = answerDao;
    }

    @Override
    @Transactional
    public void addReputaton(User sender, Answer answer) {

        Reputation reputation;

        Optional<Reputation> optionalReputation =
                reputationDao.getReputationByAnswerIdAndUserId(sender.getId(), answer.getId());

        if (!optionalReputation.isPresent()) {

            reputation = new Reputation();
            reputation.setPersistDate(LocalDateTime.now());
            reputation.setAuthor(answer.getUser());
            reputation.setSender(sender);
            reputation.setCount(10);
            reputation.setType(ReputationType.VoteAnswer);
            reputation.setAnswer(answer);

            reputationDao.persist(reputation);
        } else {
            reputation = optionalReputation.get();
            reputationDao.update(reputation);
        }
    }

    @Override
    @Transactional
    public void updateCountByDown (User sender, Long answerId) {

        Reputation newReputation = new Reputation();
        Integer downCount;
        Optional<Reputation> reputation = reputationDao.reputationExists(sender, answerId);

        if (reputation.isPresent()) {
            downCount = reputation.get().getCount() + DOWN_VOTE_POINTS;
            (reputation.get().getPersistDate().equals(LocalDateTime.now()) ? reputation.get() : newReputation).setCount(downCount);
            reputationDao.update(reputation.get());
        } else {
            downCount = DOWN_VOTE_POINTS;
            newReputation.setCount(downCount);
        }

        if (newReputation.getCount() != null) {

            Optional<Answer> answer = answerDao.getById(answerId);
            Optional<User> author = answer.map(Answer::getUser);

            newReputation.setPersistDate(LocalDateTime.now());
            newReputation.setAuthor(author.orElse(null));
            newReputation.setSender(sender);
            newReputation.setType(ReputationType.VoteAnswer);
            newReputation.setAnswer(answerDao.getById(answerId).get());

            reputationDao.persist(newReputation);
        }


    }
}
