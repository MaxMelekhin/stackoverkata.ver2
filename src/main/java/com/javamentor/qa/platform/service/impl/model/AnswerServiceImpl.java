package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements AnswerService {

    private final AnswerDao answerDao;

    public AnswerServiceImpl(ReadWriteDao<Answer, Long> readWriteDao, AnswerDao answerDao) {
        super(readWriteDao);
        this.answerDao = answerDao;
    }

    @Override
    public void markAnswerAsDeleted(Long id) {
        answerDao.markAnswerAsDeleted(id);
    }

    @Override
    public Optional<Answer> getAnswerById(Long id, Long userId) {
        return answerDao.getAnswerById(id, userId);
    }

    @Override
    public Optional<Answer> getByIdAndChecked (Long answerId, Long senderId) {
        return answerDao.getByIdAndChecked (answerId, senderId);
    }

}
