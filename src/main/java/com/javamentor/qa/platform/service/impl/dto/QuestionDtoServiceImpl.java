package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.converters.QuestionConverter;
import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class QuestionDtoServiceImpl implements QuestionDtoService {

    private final QuestionDtoDao questionDtoDao;

    public QuestionDtoServiceImpl(QuestionDtoDao questionDtoDao) {
        this.questionDtoDao = questionDtoDao;
    }

    @Override
    @Transactional
    public Optional<QuestionDto> getById(Long questionId, long authorizedUserId) {

        return questionDtoDao.getById(questionId, authorizedUserId);
    }

    @Transactional
    @Override
    public QuestionDto createNewQuestionDto(QuestionCreateDto questionCreateDto, User user) {
        return QuestionConverter.INSTANCE.questionCreateDtoToQuestionDto(questionCreateDto, user);
    }
}
