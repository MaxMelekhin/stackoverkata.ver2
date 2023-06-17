package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;

import java.util.Optional;

public interface AnswerDao extends ReadWriteDao<Answer, Long> {
    void markAnswerAsDeleted(Long id);

    Optional<Answer> getAnswerById(Long answerid, Long userId);
    Optional<Answer> getByIdAndChecked (Long answerId, Long senderId);
}
