package tech.springboot.ecommerce.data.dao;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import tech.springboot.ecommerce.data.filter.JPAFilter;

import java.util.Collections;
import java.util.List;


@Slf4j
public abstract class GenericDao<T> {
    private Class<T> classOfData;

    protected EntityManager entityManager;


    public GenericDao(Class<T> classOfData, EntityManager entityManager) {
        if (!classOfData.isAnnotationPresent(Entity.class)) {
            throw new PersistenceException("The domain class must be an Entity!");
        }
        this.classOfData = classOfData;
        this.entityManager = entityManager;
    }


    public List<T> getAll() {
        return get(null);
    }


    public List<T> get(JPAFilter filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(classOfData);
        Root<T> root = criteriaQuery.from(classOfData);
        criteriaQuery.select(root);
        if (filter != null) {
            Predicate predicate = filter.getPredicate(criteriaBuilder, root);
            if (predicate != null) {
                criteriaQuery.where(predicate);
            }
            List<Order> orderList = filter.getOrderBy(criteriaBuilder, root);
            if (!orderList.isEmpty()) {
                criteriaQuery.orderBy(orderList);
            }
        }
        try {
            TypedQuery query = entityManager.createQuery(criteriaQuery);
            if (filter != null) {
                query.setMaxResults(filter.getLimit()).setFirstResult(filter.getOffset());
            } else {
                query.setMaxResults(JPAFilter.DEFAULT_LIMIT).setFirstResult(0);
            }
            List<T> result = query.getResultList();
            return result;
        } catch (NoResultException exception) {
            return Collections.emptyList();
        }
    }


    public long count(JPAFilter filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(classOfData);
        criteriaQuery.select(criteriaBuilder.count(root));

        if (filter != null) {
            Predicate predicate = filter.getPredicate(criteriaBuilder, root);
            if (predicate != null) {
                criteriaQuery.where(predicate);
            }
        }

        try {
            TypedQuery<Long> result = entityManager.createQuery(criteriaQuery);
            return result.getSingleResult();
        }  catch (NoResultException exception) {
            return 0l;
        }
    }


    public T find(Object id) {
        return entityManager.find(classOfData, id);
    }

    public T persist(T obj) {
        entityManager.persist(obj);
        return obj;
    }


    public T insert(T obj) {
        return persist(obj);
    }

    public T merge(T obj) {
        entityManager.merge(obj);
        return obj;
    }

    public T update(T obj) {
        return merge(obj);
    }


    public void delete(String id) {
        T obj = entityManager.find(classOfData, id);
        if (obj != null) {
            entityManager.remove(obj);
        }
    }


    public void delete(JPAFilter filter) {
        if (filter == null) return;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(classOfData);
        Root<T> root = criteriaDelete.from(classOfData);

        Predicate predicate = filter.getPredicate(criteriaBuilder, root);
        if (predicate != null) {
            criteriaDelete.where(predicate);
            int result = entityManager.createQuery(criteriaDelete).executeUpdate();
            log.info("There were " + result + " items deleted");
        } else {
            log.warn("Executing a delete without any criteria. Ignoring!");
        }
    }

    /**
     * Hard deletes the object from the database
     * @param obj The object that will be deleted
     */
    public void delete(T obj) {
        entityManager.remove(entityManager.contains(obj) ? obj : entityManager.merge(obj));
    }
}
