package com.javamentor.qa.platform.service.abstracts.repository;

import java.util.List;

public interface PageDtoService <T, D>{
    List<D> getItems(Class<T> entityClass, String parameterName, Object parameterValue, int pageSize, int pageNumber);

    Long getTotalResultCount(Class<T> entityClass, String parameterName, Object parameterValue);

}
