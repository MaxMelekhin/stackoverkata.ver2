package com.javamentor.qa.platform.converters;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public abstract class QuestionConverter {

    public static final QuestionConverter INSTANCE = Mappers.getMapper( QuestionConverter.class );

    @Mapping(source = "questionDto.listTagDto", target = "tags")
    @Mapping(target = "id", ignore = true)
    public abstract Question questionDtoToQuestion(QuestionDto questionDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "user.id", target = "authorId")
    @Mapping(source = "user.fullName", target = "authorName")
    @Mapping(source = "questionCreateDto.tags", target = "listTagDto")
    public abstract QuestionDto questionCreateDtoToQuestionDto(QuestionCreateDto questionCreateDto, User user);

    @Mapping(source = "question.tags", target = "listTagDto")
    @Mapping(source = "question.user.id", target = "authorId")
    @Mapping(source = "question.user.fullName", target = "authorName")
    public abstract QuestionDto questionToQuestionDto(Question question);

}
