package com.javamentor.qa.platform.service.impl.repository;

import com.javamentor.qa.platform.dao.abstracts.repository.PageDtoDao;
import java.util.List;

public abstract class PageDtoServiceImpl<T, D> {

    private final PageDtoDao<T, D> pageDtoDao;

    public PageDtoServiceImpl(PageDtoDao<T, D> pageDtoDao) {
        this.pageDtoDao = pageDtoDao;
    }

    public abstract D convertToDto(T entity);

    public List<D> getItems(Class<T> entityClass, String parameterName, Object parameterValue, int pageSize, int pageNumber) {
       return pageDtoDao.getItems(entityClass, parameterName, parameterValue, pageSize, pageNumber);
    }

    public Long getTotalResultCount(Class<T> entityClass, String parameterName, Object parameterValue) {
        return pageDtoDao.getTotalResultCount(entityClass, parameterName, parameterValue);
    }

}
