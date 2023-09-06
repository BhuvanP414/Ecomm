package tech.springboot.ecommerce.data.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tech.springboot.ecommerce.data.domain.CategoryEntity;
import tech.springboot.ecommerce.data.domain.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductDao extends GenericDao<ProductEntity>{
    @Autowired
    public ProductDao(EntityManager entityManager) {
        super(ProductEntity.class, entityManager);
    }

    public List<ProductEntity> productsInCategory(UUID categoryId) {
        return productsInCategory(categoryId, 0, Optional.empty());
    }

    public List<ProductEntity> productsInCategory(UUID categoryId, int first, Optional<Integer> limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);

        Join<ProductEntity, List<CategoryEntity>> resource = root.join("categories");
        Predicate predicate = criteriaBuilder.equal(resource.get("id"), categoryId);

        criteriaQuery.select(root);
        criteriaQuery.where(predicate);

        TypedQuery<ProductEntity> query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(first);

        if (limit.isPresent()) {
            query.setMaxResults(limit.get());
        }

        return query.getResultList();
    }

    public long countProducts(UUID categoryId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);

        Join<ProductEntity, List<CategoryEntity>> resource = root.join("categories");
        Predicate predicate = criteriaBuilder.equal(resource.get("id"), categoryId);

        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(predicate);

        TypedQuery<Long> result = entityManager.createQuery(criteriaQuery);
        return result.getSingleResult();
    }

}
