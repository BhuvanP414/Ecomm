package tech.springboot.ecommerce.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.springboot.ecommerce.data.dao.CategoryDao;
import tech.springboot.ecommerce.data.dao.ProductDao;
import tech.springboot.ecommerce.data.domain.ProductEntity;
import tech.springboot.ecommerce.data.dto.AddProductDto;
import tech.springboot.ecommerce.data.dto.CartItemDto;
import tech.springboot.ecommerce.data.dto.ProductBasicInfoDto;
import tech.springboot.ecommerce.data.filter.JPAFilter;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDao productDao;
    private final CategoryDao categoryDao;

    public void insertNewProduct(AddProductDto addProductDto, MultipartFile file) throws IOException, URISyntaxException {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(addProductDto.getName());
        productEntity.setPrice(addProductDto.getPrice());
        productEntity.setDescription(addProductDto.getDescription());
        productEntity.setInventory(addProductDto.getInventory());

        List<UUID> categories = addProductDto.getCategories().stream()
                .filter(Objects::nonNull)
                .map(UUID::fromString)
                .collect(Collectors.toList());

        JPAFilter categoriesFilter = new JPAFilter() {
            @Override
            public Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root root) {
                return in(criteriaBuilder, root, "id", categories.toArray());
            }
        };

        productEntity.setCategories(categoryDao.get(categoriesFilter));
        ProductEntity addedProduct = productDao.insert(productEntity);

        URL filePath = ResourceUtils.getURL("uploads/images/" + addedProduct.getId() + ".png");
        Files.write(Paths.get(filePath.toURI()), file.getBytes());
    }

    public ProductEntity findById(UUID productId) {
        return productDao.find(productId);
    }

    public List<ProductEntity> productsForCategory(UUID categoryId) {
        return productDao.productsInCategory(categoryId);
    }

    public List<ProductEntity> productsForCategory(UUID categoryId, int first, int limit) {
        return productDao.productsInCategory(categoryId, first, Optional.of(limit));
    }

    public List<ProductBasicInfoDto> getBasicInfo(List<ProductEntity> products) {
        return products.stream().map(p -> new ProductBasicInfoDto(p.getId(), p.getName(), p.getPrice())).collect(Collectors.toList());
    }

    public long countProductsInCategory(UUID categoryId) {
        return productDao.countProducts(categoryId);
    }

    public List<CartItemDto> getCartItems(List<UUID> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }

        Map<UUID, Long> idsWithCount = ids.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        JPAFilter productByIds = new JPAFilter() {
            @Override
            public Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root root) {
                return in(criteriaBuilder, root, "id", ids.toArray());
            }
        };

        List<ProductEntity> productsList = productDao.get(productByIds);
        Map<UUID, ProductEntity> products = productsList.stream().collect(Collectors.toMap(ProductEntity::getId, Function.identity()));

        return idsWithCount.entrySet().stream()
                .map(p -> toCartItem(products.get(p.getKey()), p.getValue()))
                .collect(Collectors.toList());
    }

    private CartItemDto toCartItem(ProductEntity product, long numItems) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setNumItems(numItems);
        cartItemDto.setProductId(product.getId());
        cartItemDto.setPrice(product.getPrice());
        cartItemDto.setProductName(product.getName());
        cartItemDto.setTotalPrice(product.getPrice().multiply(new BigDecimal(numItems)));

        return cartItemDto;
    }
}
