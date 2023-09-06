package tech.springboot.ecommerce.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.springboot.ecommerce.data.domain.ProductEntity;
import tech.springboot.ecommerce.data.dto.AddToCartRequestDto;
import tech.springboot.ecommerce.data.dto.AddToCartResponseDto;
import tech.springboot.ecommerce.service.CategoryService;
import tech.springboot.ecommerce.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AddToCartController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @PutMapping("/cart.html")
    public AddToCartResponseDto addToCart(@RequestBody AddToCartRequestDto addToCartDto, HttpServletRequest request) throws Exception {
        ProductEntity productEntity = productService.findById(addToCartDto.getProductId());
        if (productEntity == null) {
            throw new Exception("Item not found");
        }

        List<UUID> items = (List<UUID>) request.getSession().getAttribute("CART_ITEMS_SESSION_KEY");
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(productEntity.getId());

        request.getSession().setAttribute("CART_ITEMS_SESSION_KEY", items);
        request.getSession().setAttribute("NO_CART_ITEMS_SESSION_KEY", items.size());

        return new AddToCartResponseDto(items.size());
    }
}
