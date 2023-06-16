package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.dto.CommentAnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.CommentAnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.CommentAnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user/question/{questionId}/answer")
@Tag(name = "Контроллер обработки ответов", description = "Обрабатывает входящие запросы")
public class ResourceAnswerController {
    private final AnswerDtoService answerDtoService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final VoteAnswerService voteAnswerService;
    private final ReputationService reputationService;
    private final CommentAnswerDtoService commentAnswerDtoService;
    private final CommentAnswerService commentAnswerService;

    public ResourceAnswerController(AnswerDtoService answerDtoService, QuestionService questionService, AnswerService answerService,
                                    VoteAnswerService voteAnswerService, ReputationService reputationService,
                                    CommentAnswerDtoService commentAnswerDtoService, CommentAnswerService commentAnswerService) {
        this.answerDtoService = answerDtoService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.voteAnswerService = voteAnswerService;
        this.reputationService = reputationService;
        this.commentAnswerDtoService = commentAnswerDtoService;
        this.commentAnswerService = commentAnswerService;
    }

    @ApiOperation(value = "Добавление комментария к ответу")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CommentAnswerDto.class),
            @ApiResponse(code = 404, message = "Ответ по заданному id не был найден")})
    @PostMapping("/{answerId}/comment")
    public ResponseEntity<CommentAnswerDto> addCommentToAnswer(@RequestBody String comment,
                                                               @PathVariable @ApiParam(name = "questionId", value = "id вопроса") long questionId,
                                                               @PathVariable @ApiParam(name = "answerId", value = "id ответа") long answerId,
                                                               @AuthenticationPrincipal User user) {
        if (answerService.getById(answerId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CommentAnswer commentAnswer = commentAnswerService.addCommentToAnswer(comment, user, answerId);
        CommentAnswerDto commentAnswerDto = commentAnswerDtoService.getCommentAnswerDtoByAnswerIdAndCommentId(answerId,
                commentAnswer.getComment().getId());
        return new ResponseEntity<>(commentAnswerDto, HttpStatus.OK);
    }


    @GetMapping
    @ApiOperation(value = "Возвращает список ответ по заданному идентификатору вопроса")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AnswerDto.class),
            @ApiResponse(code = 400, message = "Недопустимый запрос"),
            @ApiResponse(code = 404, message = "Ответ по заданному id не был найден")
    })
    public ResponseEntity<List<AnswerDto>> getAllAnswers(@PathVariable("questionId") Long questionId,
                                                         @AuthenticationPrincipal User user) {
        if (questionService.getById(questionId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(answerDtoService.getAllAnswersDtoByQuestionId(questionId, user.getId()));
    }



    @DeleteMapping("/{answerId}")
    @ApiOperation("Метод, помечающий ответ на удаление")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ответ успешно помечен для удаления"),
            @ApiResponse(code = 401, message = "Пользователь не авторизирован"),
            @ApiResponse(code = 403, message = "Доступ запрещен"),
            @ApiResponse(code = 404, message = "Ответ не найден")
    })
    public ResponseEntity<HttpStatus> deleteAnswerById(@PathVariable Long answerId) {
        if (!answerService.existsById(answerId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        answerService.markAnswerAsDeleted(answerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @PostMapping(value = "/{id}/downVote")
    @ApiOperation(
            value = "отдать голос за Ответ, который уменьшает оценку Ответа и вычитает 5 баллов из Репутации автора Ответа",
            notes = """
                    Api downVote ничего не получает, а возвращает общее количество голосов (сумму up and down vote)
                    Когда пользователь голосует за ответ нужно уменьшить репутацию автору ответа:
                    * за down - 5 к репутации
                    Пользователь должен быть аутентифицирован для того, чтобы проголосовать
                    """)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Вы успешно проголосовали"),
                    @ApiResponse(code = 401, message = "Неавторизованный пользователь не может проголосовать"),
                    @ApiResponse(code = 404, message = "Ответ/вопрос по заданному id не был найден"),
                    @ApiResponse(code = 405, message = "Нельзя голосовать за свой ответ"),
                    @ApiResponse(code = 409, message = "Вы уже проголосовали отрицательно за этот ответ")
            })
    public ResponseEntity<Long> downVote (
            @ApiParam(value = "id Вопроса", required = true) @PathVariable("questionId") Long questionId,
            @ApiParam(value = "id Ответа", required = true) @PathVariable("id") Long answerId,
            @AuthenticationPrincipal User sender) {

        // user
        if (sender == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // question
        if (questionService.getById(questionId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // answer
        if (answerService.getByIdAndChecked(answerId, sender.getId()).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        // vote
        if (voteAnswerService.voteAnswerExists(answerId, sender.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        // reputation
        reputationService.updateCountByDown(sender, answerId);

        return ResponseEntity.ok(voteAnswerService.getVoteAnswerAmount(answerId));
    }

    @PostMapping("/{id}/upVote")
    @ApiOperation(
            value = "Проголосовать за ответ",
            notes = "Увеличивает количество голосов за ответ на 1 и возвращает общее количество голосов " +
                    "Увеличивает количество очков репутации на +10 очков автору ответа")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно увеличено количество голосов и возвращено общее количество голосов"),
            @ApiResponse(code = 401, message = "Пользователь не авторизирован"),
            @ApiResponse(code = 404, message = "Ответ не найден")
    })
    public ResponseEntity<Long> getTotalVotesCount(
            @ApiParam(name = "id", value = "ID ответа", required = true)
            @PathVariable() Long id,
            @AuthenticationPrincipal User user) {

        Optional<Answer> optionalAnswer = answerService.getAnswerById(id, user.getId());

        if (!optionalAnswer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Answer answer = optionalAnswer.get();

        voteAnswerService.voteUpToAnswer(user, answer);

        reputationService.addReputaton(user, answer);



        return new ResponseEntity<>(voteAnswerService.getAllTheVotesForThisAnswer(answer.getId()), HttpStatus.OK);
    }

}
