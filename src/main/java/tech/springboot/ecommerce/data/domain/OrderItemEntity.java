package tech.springboot.ecommerce.data.domain;

import lombok.Data;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "order_items_to_product_fk"))
    private ProductEntity product;

    private BigDecimal price;
}
