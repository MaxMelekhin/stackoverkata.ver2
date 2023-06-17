package com.javamentor.qa.platform.service.abstracts.repository;

import java.util.List;
import java.util.Map;

public interface PageDtoService<T, D> {
    List<D> getItems(Map<String, Object> parameters) throws Exception;

    Long getTotalResultCount(Map<String, Object> parameters) throws Exception;

}
