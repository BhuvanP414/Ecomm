package tech.springboot.ecommerce.data.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", foreignKey = @ForeignKey(name = "categories_fk"))
    private CategoryEntity parent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", foreignKey = @ForeignKey(name = "categories_fk"))
    private List<CategoryEntity> children;
}
