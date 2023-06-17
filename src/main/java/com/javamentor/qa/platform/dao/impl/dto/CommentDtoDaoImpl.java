package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentDtoDaoImpl implements CommentDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionCommentDto> getAllQuestionCommentDtoById(Long questionId) {
        return entityManager.createQuery("""
                        select new com.javamentor.qa.platform.models.dto.QuestionCommentDto(cq.comment.id,
                         cq.question.id as questionId,
                         cq.comment.lastUpdateDateTime as lastUpdateDate,
                         cq.comment.persistDateTime as persistDate,
                         cq.comment.text as text,
                         cq.comment.user.id as userId,
                         cq.comment.user.imageLink as imageLink,
                         (select coalesce(sum(r.count),0) 
                         from Reputation r where r.author.id = cq.comment.user.id) as reputation) 
                         from CommentQuestion cq where cq.question.id = :questionId 
                          group by cq.comment.id,
                           cq.question.id, 
                           cq.comment.lastUpdateDateTime, 
                           cq.comment.persistDateTime, 
                           cq.comment.text,
                           cq.comment.user.id,
                           cq.comment.user.imageLink       
                        """, QuestionCommentDto.class)
                .setParameter("questionId", questionId)
                .getResultList();
    }
}
