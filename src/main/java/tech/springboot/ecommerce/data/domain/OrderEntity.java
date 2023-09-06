package tech.springboot.ecommerce.data.domain;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "orders_fk"))
    private UserEntity user;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "order_items_to_order_fk"))
    private List<OrderItemEntity> items;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
