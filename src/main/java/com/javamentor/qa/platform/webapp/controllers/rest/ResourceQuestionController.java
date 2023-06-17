package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.CommentDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/question")
@Tag(name = "Контроллер вопросов")
public class ResourceQuestionController {

    private final UserService userService;
    private final QuestionService questionService;
    private final QuestionDtoService questionDtoService;
    private final TagService tagService;
    private  final CommentDtoService commentDtoService;

    @Autowired
    public ResourceQuestionController(UserService userService, QuestionService questionService, QuestionDtoService questionDtoService, TagService tagService, CommentDtoService commentDtoService) {
        this.userService = userService;
        this.questionService = questionService;
        this.questionDtoService = questionDtoService;
        this.tagService = tagService;
        this.commentDtoService = commentDtoService;
    }

    @GetMapping("/{id}/comment")
    @Operation(summary = "получение всех комментариев к вопросу по идентификатору (id)")
    @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен")
    @ApiResponse(responseCode = "404", description = "Вопрос по данному id не найден")
    public ResponseEntity<List<QuestionCommentDto>> getAllCommentsOnQuestion(@PathVariable Long id) {

        if (!questionService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(commentDtoService.getAllQuestionCommentDtoById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение данных о вопросе по его уникальному идентификатору (id)")
    @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен")
    @ApiResponse(responseCode = "404", description = "Вопрос по данному id не найден")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable Long id,
                                                       @AuthenticationPrincipal User user) {

        return questionDtoService.getById(id, user.getId()).map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(new QuestionDto(), HttpStatus.NOT_FOUND));
    }


    @GetMapping("/count")
    @Operation(summary = "Получение количества всех вопросе в бд")
    @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен")
    public ResponseEntity<Long> getCountQuestion() {

        return new ResponseEntity<>(questionService.getCountQuestion(), HttpStatus.OK);
    }


    @PostMapping
    @Operation(summary = "Добавление нового вопроса")
    @ApiResponse(responseCode = "200", description = "Вопрос добавлен")
    @ApiResponse(responseCode = "400", description = "Ошибка введенных данных")
    public ResponseEntity<QuestionDto> questionCreate (@RequestBody QuestionCreateDto questionCreateDto) {
        // TODO: Доставать юзера из секьюрити
        // User user = userService.getByEmail(principal.getName()).orElse(null);

        User user = userService.getByEmail("user@mail.ru").orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        QuestionDto questionDto = questionDtoService.createNewQuestionDto(questionCreateDto, user);
        Question question = tagService.checkTag(questionDto);

        question.setUser(user);
        questionService.persist(question);

        return new ResponseEntity<>(questionDto, HttpStatus.OK);
    }
}

