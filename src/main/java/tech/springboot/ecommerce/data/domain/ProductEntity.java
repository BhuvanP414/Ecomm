package tech.springboot.ecommerce.data.domain;

import lombok.Data;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer inventory;

    @ManyToMany
    @JoinTable(name = "product_to_category",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")},
            foreignKey = @ForeignKey(name = "category_to_product_fk"),
            inverseForeignKey = @ForeignKey(name = "product_to_category_fk"))
    private List<CategoryEntity> categories;
}
