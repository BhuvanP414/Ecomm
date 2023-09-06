package tech.springboot.ecommerce.data.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import tech.springboot.ecommerce.data.domain.UserEntity;
import tech.springboot.ecommerce.data.filter.JPAFilter;

@Repository
public class UserDao extends GenericDao<UserEntity> {
    public UserDao(EntityManager entityManager) {
        super(UserEntity.class, entityManager);
    }

    public UserEntity withEmail(String email) {
        JPAFilter filter = new JPAFilter() {
            @Override
            public Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root root) {
                return equalsIgnoreCase(criteriaBuilder, root, "email", email);
            }
        };

        return get(filter).stream().findFirst().orElse(null);
    }
}
