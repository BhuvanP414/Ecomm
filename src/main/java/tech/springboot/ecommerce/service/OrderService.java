package tech.springboot.ecommerce.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.springboot.ecommerce.data.dao.OrderDao;
import tech.springboot.ecommerce.data.dao.ProductDao;
import tech.springboot.ecommerce.data.dao.UserDao;
import tech.springboot.ecommerce.data.domain.*;
import tech.springboot.ecommerce.data.filter.JPAFilter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserDao userDao;
    private final ProductDao productDao;

    private final OrderDao orderDao;

    public UUID doOrder(List<UUID> cartItems, UUID userId) {
        JPAFilter productByIds = new JPAFilter() {
            @Override
            public Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root root) {
                return in(criteriaBuilder, root, "id", cartItems.toArray());
            }
        };

        UserEntity user = userDao.find(userId);
        List<ProductEntity> productsList = productDao.get(productByIds);
        Map<UUID, Long> idsWithCount = cartItems.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        productsList.forEach(p -> {
            p.setInventory(p.getInventory() - idsWithCount.get(p.getId()).intValue());
            productDao.update(p);
        });

        Map<UUID, ProductEntity> products = productsList.stream().collect(Collectors.toMap(ProductEntity::getId, Function.identity()));
        OrderEntity orderEntity = buildOrder(products, idsWithCount, user);
        orderEntity = orderDao.persist(orderEntity);

        return orderEntity.getId();
    }

    private OrderEntity buildOrder(Map<UUID, ProductEntity> products, Map<UUID, Long> idsWithCount, UserEntity user) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus(OrderStatus.ORDER_PLACED);
        orderEntity.setOrderDate(LocalDateTime.now());
        orderEntity.setUser(user);
        orderEntity.setItems(buildOrderItems(products, idsWithCount));
        return orderEntity;
    }

    private List<OrderItemEntity> buildOrderItems(Map<UUID, ProductEntity> products, Map<UUID, Long> idsWithCount) {
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        idsWithCount.entrySet().forEach(itemWithCount -> {
            for (int count = 0; count < itemWithCount.getValue(); count++) {
                orderItemEntities.add(fromProductEntity(products.get(itemWithCount.getKey())));
            }
        });
        return orderItemEntities;
    }

    private OrderItemEntity fromProductEntity(ProductEntity productEntity) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProduct(productEntity);
        orderItemEntity.setPrice(productEntity.getPrice());
        return orderItemEntity;
    }
}
