package com.javamentor.qa.platform.dao.impl.repository;

import com.javamentor.qa.platform.dao.abstracts.repository.PageDtoDao;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.ApplicationContext;
import javax.persistence.TypedQuery;

public abstract class PageDtoDaoImpl<E, D> implements PageDtoDao<E, D> {
    @PersistenceContext
    private EntityManager entityManager;

    private final ApplicationContext applicationContext;

    public PageDtoDaoImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private Class<E> clazz = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];

    public abstract D convertToDto(E entity);

    public List<D> getItems(Map<String, Object> parameters) throws Exception {
        if ((parameters.containsKey("workPagination")) && parameters.get("workPagination") != null) {
            try {
                return invokeGetItems(parameters).stream().map(this::convertToDto).collect(Collectors.toList());
            } catch (Exception e) {
                throw new Exception("Не нашел нужной реализации пагинации", e);
            }
        }
        return defaultGetItems(parameters);
    }

    public Long getTotalResultCount(Map<String, Object> parameters) throws Exception {
        if ((parameters.containsKey("workPagination")) && parameters.get("workPagination") != null) {
            try {
                return invokeGetTotalResultCount(parameters);
            } catch (Exception e) {
                throw new Exception("Не нашел нужной реализации пагинации", e);
            }
        }
        return defaultGetTotalResultCount(parameters);
    }

    private Long defaultGetTotalResultCount(Map<String, Object> parameters) {
        return entityManager.createQuery("select count(e) FROM " + clazz.getSimpleName() + " e", Long.class).getSingleResult();
    }

    private List<D> defaultGetItems(Map<String, Object> parameters) {
        Integer currentPage = (Integer) parameters.get("currentPage");
        Integer itemsOnPage = (Integer) parameters.get("itemsOnPage");

        TypedQuery<E> query = entityManager.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e", clazz);
        query.setFirstResult((currentPage - 1) * (itemsOnPage));
        query.setMaxResults(itemsOnPage);
        return query.getResultList().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private Long invokeGetTotalResultCount(Map<String, Object> parameters) throws Exception {
        Object bean = applicationContext.getBean(parameters.get("workPagination").toString());
        try {
            Class<?>[] parameterTypes = {Map.class};
            Object[] parameterValues = new Object[]{parameters};
            return (Long) bean.getClass().getMethod("getTotalResultCount", parameterTypes).invoke(bean, parameterValues);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new Exception("Проблемы с реализацией метода getItems кастомной пагинации", e);
        }
    }

    private List<E> invokeGetItems(Map<String, Object> parameters) throws Exception {
        Object bean = applicationContext.getBean(parameters.get("workPagination").toString());
        try {
            Class<?>[] parameterTypes = {Map.class};
            Object[] parameterValues = new Object[]{parameters};
            return (List<E>) bean.getClass().getMethod("getItems", parameterTypes).invoke(bean, parameterValues);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new Exception("Проблемы с реализацией метода getTotalResultPage  кастомной пагинации", e);
        }
    }
}
