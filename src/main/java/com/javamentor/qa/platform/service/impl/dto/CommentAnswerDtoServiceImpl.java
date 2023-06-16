package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentAnswerDtoDao;
import com.javamentor.qa.platform.models.dto.CommentAnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.CommentAnswerDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentAnswerDtoServiceImpl implements CommentAnswerDtoService {

    private final CommentAnswerDtoDao commentAnswerDtoDao;

    public CommentAnswerDtoServiceImpl(CommentAnswerDtoDao commentAnswerDtoDao) {
        this.commentAnswerDtoDao = commentAnswerDtoDao;
    }

    @Override
    @Transactional(readOnly = true)
    public CommentAnswerDto getCommentAnswerDtoByAnswerIdAndCommentId(long answerId, long commentId) {
        return commentAnswerDtoDao.getCommentAnswerDtoByAnswerIdAndCommentId(answerId, commentId);
    }
}
