package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentAnswerDtoDao;
import com.javamentor.qa.platform.models.dto.CommentAnswerDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CommentAnswerDtoDaoImpl implements CommentAnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CommentAnswerDto getCommentAnswerDtoByAnswerIdAndCommentId(long answerId, long commentId) {
        return (CommentAnswerDto) entityManager.createQuery("""
                        SELECT c.id as id,
                        a.id as answerId,
                        c.lastUpdateDateTime as lastRedactionDate,
                        a.persistDateTime as persistDate,
                        c.text as text,
                        c.user.id as userId,
                        u.imageLink as imageLink,
                        r.count as reputation
                        from Comment as c, 
                        Answer as a,
                        CommentAnswer as c_a, 
                        User as u,
                        Reputation as r
                        where c.id = c_a.comment.id and
                        a.id = c_a.answer.id and
                        a.id = :answerId and
                        c.id = :commentId and
                        c.user.id = u.id and
                        u.id = r.author.id
                        """).setParameter("answerId", answerId)
                .setParameter("commentId", commentId)
                .getSingleResult();
    }
}
