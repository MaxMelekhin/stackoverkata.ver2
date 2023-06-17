package com.javamentor.qa.platform.service.impl.model;
import com.javamentor.qa.platform.converters.QuestionConverter;
import com.javamentor.qa.platform.converters.TagConverter;
import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends ReadWriteServiceImpl<Tag, Long> implements TagService {

    private final TagDao tagDao;
    @Autowired
    public TagServiceImpl(ReadWriteDao<Tag, Long> readWriteDao, TagDao tagDao) {
        super(readWriteDao);
        this.tagDao = tagDao;
    }

    @Override
    public Optional<Tag> getByName(String name) {
        return tagDao.getByName(name);
    }

    @Transactional
    @Override
    public Question checkTag(QuestionDto questionDto) {
        Question question = QuestionConverter.INSTANCE.questionDtoToQuestion(questionDto);
        question.setTags(new ArrayList<>());

        List<Tag> tags = questionDto.getListTagDto().stream().
                map(TagConverter.INSTANCE::tagDtoToTag).collect(Collectors.toList());

        tags.forEach(tag -> {
            if (tagDao.getByName(tag.getName()).orElse(null) == null) {
                tag.setPersistDateTime(LocalDateTime.now());
                tag.setDescription("Write description");
                tagDao.persist(tag);
            }
            question.getTags().add(tagDao.getByName(tag.getName()).orElse(null));
        });

        return question;
    }
}
