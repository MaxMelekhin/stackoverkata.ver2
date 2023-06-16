package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.CommentAnswerService;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentAnswerServiceImpl extends ReadWriteServiceImpl<CommentAnswer, Long> implements CommentAnswerService {

    private final AnswerDao answerDao;

    public CommentAnswerServiceImpl(ReadWriteDao<CommentAnswer, Long> readWriteDao, AnswerDao answerDao) {
        super(readWriteDao);
        this.answerDao = answerDao;
    }

    @Transactional
    @Override
    public CommentAnswer addCommentToAnswer(String comment, User user, long answerId) {
        if (comment == null || comment.length() == 0)
            throw new IllegalArgumentException("Комментарий не может быть пустой или null");
        CommentAnswer commentAnswer = new CommentAnswer(comment, user);
        commentAnswer.setAnswer(answerDao.getById(answerId).get());
        persist(commentAnswer);
        return commentAnswer;
    }
}