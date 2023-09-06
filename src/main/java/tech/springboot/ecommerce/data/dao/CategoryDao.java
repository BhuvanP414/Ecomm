package tech.springboot.ecommerce.data.dao;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tech.springboot.ecommerce.data.domain.CategoryEntity;

@Repository
public class CategoryDao extends GenericDao<CategoryEntity>{
    @Autowired
    public CategoryDao(EntityManager entityManager) {
        super(CategoryEntity.class, entityManager);
    }
}
