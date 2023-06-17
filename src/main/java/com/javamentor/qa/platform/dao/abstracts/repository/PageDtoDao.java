package com.javamentor.qa.platform.dao.abstracts.repository;

import java.util.List;
import java.util.Map;

public interface PageDtoDao<E, D> {
    List<D> getItems(Map<String, Object> parameters) throws Exception;

    Long getTotalResultCount(Map<String, Object> parameters) throws Exception;

}
