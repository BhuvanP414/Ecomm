package tech.springboot.ecommerce.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tech.springboot.ecommerce.data.domain.CategoryEntity;
import tech.springboot.ecommerce.data.domain.UserEntity;
import tech.springboot.ecommerce.data.dto.CartItemDto;
import tech.springboot.ecommerce.service.CategoryService;
import tech.springboot.ecommerce.service.OrderService;
import tech.springboot.ecommerce.service.ProductService;
import tech.springboot.ecommerce.utils.CategoryTreeMaker;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;

    @GetMapping("/cart.html")
    public String renderCart(Model model, HttpServletRequest request) {
        List<CartItemDto> cartItems = productService.getCartItems((List<UUID>) request.getSession().getAttribute("CART_ITEMS_SESSION_KEY"));

        List<CategoryEntity> rootCategories = categoryService.findTopLevelCategories();
        List<Pair<String, String>> categories = CategoryTreeMaker.buildTree(rootCategories, "");

        BigDecimal total = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getNumItems())))
                .reduce(BigDecimal.ZERO, (sum, item) -> sum.add(item));

        model.addAttribute("products", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("categories", categories);

        return "cart.html";
    }

    @Transactional
    @PostMapping("/buy.html")
    public String buyCart(HttpServletRequest request, Principal principal) {
        List<UUID> cartItems = (List<UUID>) request.getSession().getAttribute("CART_ITEMS_SESSION_KEY");
        UUID userId = ((UserEntity)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();

        UUID orderId = orderService.doOrder(cartItems, userId);

        request.getSession().removeAttribute("CART_ITEMS_SESSION_KEY");
        request.getSession().removeAttribute("NO_CART_ITEMS_SESSION_KEY");

        return "orderPlaced.html";
    }
}
