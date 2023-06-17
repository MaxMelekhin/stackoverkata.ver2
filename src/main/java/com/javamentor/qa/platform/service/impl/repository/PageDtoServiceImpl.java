package com.javamentor.qa.platform.service.impl.repository;

import com.javamentor.qa.platform.dao.abstracts.repository.PageDtoDao;
import java.util.List;
import java.util.Map;

public abstract class PageDtoServiceImpl<E, D> {

    private final PageDtoDao<E, D> pageDtoDao;

    public PageDtoServiceImpl(PageDtoDao<E, D> pageDtoDao) {
        this.pageDtoDao = pageDtoDao;
    }

    public abstract D convertToDto(E entity);

    public List<D> getItems(Map<String, Object> parameters) throws Exception {
        return pageDtoDao.getItems(parameters);
    }

    public Long getTotalResultCount(Map<String, Object> parameters) throws Exception {
        return pageDtoDao.getTotalResultCount(parameters);
    }

}
