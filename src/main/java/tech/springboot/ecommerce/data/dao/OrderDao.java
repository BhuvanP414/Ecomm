package tech.springboot.ecommerce.data.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import tech.springboot.ecommerce.data.domain.OrderEntity;

@Repository
public class OrderDao extends GenericDao<OrderEntity> {
    public OrderDao(EntityManager entityManager) {
        super(OrderEntity.class, entityManager);
    }
}
