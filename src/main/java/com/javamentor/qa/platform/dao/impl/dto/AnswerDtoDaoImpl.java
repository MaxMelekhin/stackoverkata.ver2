package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDtoDaoImpl implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AnswerDto> getAllAnswersDtoByQuestionId(Long questionId, Long userId) {
        return entityManager.createQuery("""
                SELECT
                a.id as answerId,
                a.user.id as userId,
                a.question.id as questionId,
                a.htmlBody as answerText,
                a.persistDateTime as persistDateTime,
                (SELECT COALESCE(SUM(CASE WHEN va.voteType = 'UP' THEN 1 WHEN va.voteType = 'DOWN' THEN -1 END ),0)
                FROM VoteAnswer va WHERE va.answer.id = a.id) as utilityAnswer,
                a.dateAcceptTime as dateAcceptTime,
                a.isHelpful as answerRating,
                (SELECT COALESCE(SUM(r.count),0)
                FROM Reputation r WHERE r.author.id = a.user.id) as userRating,
                a.user.imageLink as imageLink,
                a.user.nickname as nickName,
                (SELECT COUNT(va.id)
                FROM VoteAnswer va WHERE va.answer.id = a.id) as countVote,
                (SELECT COALESCE(va.voteType, null)
                FROM VoteAnswer va WHERE va.answer.id = a.id AND va.user.id =:userId) as voteType
                FROM Answer a WHERE a.question.id = :questionId AND a.isDeleted = FALSE
                GROUP BY
                a.id,
                a.user.id,
                a.question.id,
                a.htmlBody,
                a.persistDateTime,
                a.isHelpful,
                a.dateAcceptTime,
                a.user.imageLink,
                a.user.nickname
                """, AnswerDto.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .getResultList();
    }
}