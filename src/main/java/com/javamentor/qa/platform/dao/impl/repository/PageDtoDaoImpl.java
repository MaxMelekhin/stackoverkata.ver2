package com.javamentor.qa.platform.dao.impl.repository;

import com.javamentor.qa.platform.dao.abstracts.repository.PageDtoDao;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class PageDtoDaoImpl<T, D> implements PageDtoDao<T, D> {
    @PersistenceContext
    private EntityManager entityManager;

    public abstract D convertToDto(T entity);

    public List<D> getItems(Class<T> entityClass, String parameterName, Object parameterValue, int pageSize, int pageNumber) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> from = criteriaQuery.from(entityClass);

        if (!parameterValue.toString().equals("")) {
            Predicate parameterPredicate = criteriaBuilder.equal(from.get(parameterName), parameterValue);
            criteriaQuery.where(parameterPredicate);
            TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult((pageNumber - 1) * pageSize);
            typedQuery.setMaxResults(pageSize);
            return typedQuery.getResultList().stream().map(e -> convertToDto(e)).collect(Collectors.toList());
        }

        criteriaQuery.orderBy(criteriaBuilder.asc(from.get(parameterName)));
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList().stream().map(e -> convertToDto(e)).collect(Collectors.toList());

    }


    public Long getTotalResultCount(Class<T> entityClass, String parameterName, Object parameterValue) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> from = criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaBuilder.count(from));

        if (!parameterValue.toString().equals("")) {
            Predicate parameterPredicate = criteriaBuilder.equal(from.get(parameterName), parameterValue);
            criteriaQuery.where(parameterPredicate);
            TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
            return typedQuery.getSingleResult();
        }

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getSingleResult();
    }

}