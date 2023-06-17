package com.javamentor.qa.platform.converters;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class TagConverter {

    public static final TagConverter INSTANCE = Mappers.getMapper( TagConverter.class );

    public abstract Tag tagDtoToTag(TagDto tagDto);

}
