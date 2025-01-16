package br.ufrn.imd.SIGResAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.SIGResAPI.dto.OrderDTO;
import br.ufrn.imd.SIGResAPI.dto.SaleDTO;
import br.ufrn.imd.SIGResAPI.models.Desk;
import br.ufrn.imd.SIGResAPI.models.Order;
import br.ufrn.imd.SIGResAPI.models.Product;
import br.ufrn.imd.SIGResAPI.models.ProductVariant;
import br.ufrn.imd.SIGResAPI.models.User;
import br.ufrn.imd.SIGResAPI.repository.DeskRepository;
import br.ufrn.imd.SIGResAPI.repository.OrderRepository;
import br.ufrn.imd.SIGResAPI.repository.ProductRepository;
import br.ufrn.imd.SIGResAPI.repository.ProductVariantRepository;
import br.ufrn.imd.SIGResAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DeskRepository deskRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductVariantRepository productVariantRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SaleController saleController;

    @GetMapping
    public ResponseEntity<List<Order>> allOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> order(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return ResponseEntity.ok(order);
    }

    @GetMapping("/by-desk/{id}")
    public ResponseEntity<List<Order>> allOrdersByDesk(@PathVariable Long id) {
        Desk desk = deskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Desk not found"));
        List<Order> activeOrdersInDesk = orderRepository.findByDesk(desk);
        activeOrdersInDesk.removeIf(order -> !order.isActive());
        return ResponseEntity.ok(activeOrdersInDesk);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO body) {

        Desk desk = deskRepository.findById(body.deskId())
                .orElseThrow(() -> new RuntimeException("Desk not found"));
        if (!desk.isActive() || !desk.isFill()) {
            return ResponseEntity.badRequest().build();
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userRepository.existsById(user.getId())) {
            return ResponseEntity.badRequest().build();
        }

        Order order = new Order();
        order.setActive(true);
        order.setAmount(body.amount());
        order.setCreatedBy(user);
        order.setDesk(desk);
        if (body.isVariant()) {
            ProductVariant productVariant = productVariantRepository.findById(body.productId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setProductVariant(productVariant);
            SaleDTO saleDTO = new SaleDTO(true, productVariant.getId(), body.amount(), user.getId());
            saleController.doSale(saleDTO);
        } else {
            Product product = productRepository.findById(body.productId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setProduct(product);
            SaleDTO saleDTO = new SaleDTO(false, product.getId(), body.amount(), user.getId());
            saleController.doSale(saleDTO);
        }
        order.setTime(TimeController.getLocalDateTime());

        orderRepository.save(order);

        return ResponseEntity.ok(order);
    }

    public void disableOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setActive(false);
        orderRepository.save(order);
    }

}
