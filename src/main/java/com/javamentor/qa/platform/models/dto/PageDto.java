package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Страница пагинации")
public class PageDto<T> {
    @Schema(description = "Номер текущей страницы")
    private int currentPageNumber;
    @Schema(description = "Общее число страниц")
    private int totalPageCount;
    @Schema(description = "Общее число сущностей с заданными параметрами")
    private int totalResultCount;
    @Schema(description = "Список Dto сущностей")
    private List<T> items;
    @Schema(description = "Количество сущностей отображаемых на одной странице")
    private int itemsOnPage;
}
