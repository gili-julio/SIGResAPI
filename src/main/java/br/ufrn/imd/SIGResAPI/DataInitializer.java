package br.ufrn.imd.SIGResAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.imd.SIGResAPI.controller.SaleController;
import br.ufrn.imd.SIGResAPI.controller.TimeController;
import br.ufrn.imd.SIGResAPI.dto.SaleDTO;
import br.ufrn.imd.SIGResAPI.enums.ERole;
import br.ufrn.imd.SIGResAPI.models.Desk;
import br.ufrn.imd.SIGResAPI.models.Order;
import br.ufrn.imd.SIGResAPI.models.Product;
import br.ufrn.imd.SIGResAPI.models.ProductVariant;
import br.ufrn.imd.SIGResAPI.models.Role;
import br.ufrn.imd.SIGResAPI.models.SystemConfig;
import br.ufrn.imd.SIGResAPI.models.User;
import br.ufrn.imd.SIGResAPI.repository.DeskRepository;
import br.ufrn.imd.SIGResAPI.repository.OrderRepository;
import br.ufrn.imd.SIGResAPI.repository.ProductRepository;
import br.ufrn.imd.SIGResAPI.repository.ProductVariantRepository;
import br.ufrn.imd.SIGResAPI.repository.RoleRepository;
import br.ufrn.imd.SIGResAPI.repository.SystemConfigRepository;
import br.ufrn.imd.SIGResAPI.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final DeskRepository deskRepository;
    private final OrderRepository orderRepository;
    private final SaleController saleController;
    private final SystemConfigRepository configRepository;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder, ProductRepository productRepository,
            ProductVariantRepository productVariantRepository, DeskRepository deskRepository,
            SaleController saleController, OrderRepository orderRepository, SystemConfigRepository configRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
        this.deskRepository = deskRepository;
        this.saleController = saleController;
        this.orderRepository = orderRepository;
        this.configRepository = configRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Verifica se já existe o role "ROLE_ADMIN"
        Optional<Role> adminOptRole = roleRepository.findByName(ERole.ROLE_ADMIN);
        Role adminRole = new Role();
        if (adminOptRole.isEmpty()) {
            adminRole.setName(ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);
        } else {
            adminRole = adminOptRole.get();
        }
        // Verifica se o usuário "admin" já existe
        Optional<User> adminOptUser = userRepository.findByUsername("admin");
        User adminUser = new User();
        if (adminOptUser.isEmpty()) {
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            userRepository.save(adminUser);
        } else {
            adminUser = adminOptUser.get();
        }
        // Verifica se o admin já possui o role "ROLE_ADMIN"
        if (!adminUser.getRoles().contains(adminRole)) {
            adminUser.getRoles().add(adminRole);
            userRepository.save(adminUser);
        }

        // Verifica se já existe o role "ROLE_USER"
        Optional<Role> userOptRole = roleRepository.findByName(ERole.ROLE_USER);
        Role userRole = new Role();
        if (userOptRole.isEmpty()) {
            userRole.setName(ERole.ROLE_USER);
            roleRepository.save(userRole);
        } else {
            userRole = userOptRole.get();
        }
        // Verifica se o usuário "user" já existe
        Optional<User> userOptUser = userRepository.findByUsername("user");
        User userUser = new User();
        if (userOptUser.isEmpty()) {
            userUser.setUsername("user");
            userUser.setPassword(passwordEncoder.encode("user"));
            userRepository.save(userUser);
        } else {
            userUser = userOptUser.get();
        }
        // Verifica se o usuário já possui o role "ROLE_USER"
        if (!userUser.getRoles().contains(userRole)) {
            userUser.getRoles().add(userRole);
            userRepository.save(userUser);
        }
        // Verifica se o admin já possui o role "ROLE_USER"
        if (!adminUser.getRoles().contains(userRole)) {
            adminUser.getRoles().add(userRole);
            userRepository.save(adminUser);
        }

        // Para finalidades de teste
        // A api vai ser inicializada com alguns dados genéricos

        // Users
        for (int i = 1; i <= 20; i++) {
            Optional<User> optUser = userRepository.findByUsername("Teste_" + i);
            User testUser = new User();
            if (optUser.isEmpty()) {
                testUser.setUsername("Teste_" + i);
                testUser.setPassword(passwordEncoder.encode("teste"));
            } else {
                testUser = optUser.get();
            }
            if (!testUser.getRoles().contains(userRole)) {
                testUser.getRoles().add(userRole);
                userRepository.save(testUser);
            }
        }

        // Produtos e suas variantes
        for (int i = 1; i <= 20; i++) {
            Long productId = Long.valueOf(i);
            Optional<Product> optProduct = productRepository.findById(productId);
            Product product = new Product();
            if (!optProduct.isPresent()) {
                product.setAmount(100);
                product.setInHappyHour(true);
                product.setName("Produto " + i);
                product.setPrice(10 * i);
                product.setPriceInHappyHour(5 * i);
                productRepository.save(product);
            } else {
                product = optProduct.get();
            }
            // Variantes
            List<ProductVariant> variants = productVariantRepository.findByProductOrderByIdAsc(product);
            if (variants.isEmpty()) {
                ProductVariant variant1 = new ProductVariant();
                variant1.setAmount(20);
                variant1.setInHappyHour(true);
                variant1.setName("Variante 1 do " + product.getName());
                variant1.setPrice(i * 3);
                variant1.setPriceInHappyHour(i * 2);
                variant1.setProduct(product);
                productVariantRepository.save(variant1);
                ProductVariant variant2 = new ProductVariant();
                variant2.setAmount(20);
                variant2.setInHappyHour(true);
                variant2.setName("Variante 2 do " + product.getName());
                variant2.setPrice(i * 3);
                variant2.setPriceInHappyHour(i * 2);
                variant2.setProduct(product);
                productVariantRepository.save(variant2);
            }
        }

        // Mesas
        List<Desk> desksToCreate = new ArrayList<>();
        for (Long i = 1L; i <= 1000L; i++) {
            if (i % 2 == 0) {
                if (!deskRepository.existsById(i)) {
                    desksToCreate.add(new Desk(i, true, true, null));
                }
            } else {
                if (!deskRepository.existsById(i)) {
                    desksToCreate.add(new Desk(i, true, false, null));
                }
            }
        }
        deskRepository.saveAll(desksToCreate);

        // Pedidos
        for (int i = 1; i <= 10; i++) {
            Long deskId = Long.valueOf(i);
            Optional<Order> optOrder = orderRepository.findById(deskId);
            if (!optOrder.isPresent()) {
                Desk desk = deskRepository.findById(deskId)
                        .orElseThrow(() -> new RuntimeException("Desk not found"));
                User user = userRepository.findById(deskId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                Order order = new Order();
                order.setActive(true);
                order.setAmount(10);
                order.setCreatedBy(user);
                order.setDesk(desk);
                if (i % 2 == 0) {
                    ProductVariant productVariant = productVariantRepository.findById(deskId)
                            .orElseThrow(() -> new RuntimeException("Product Variant not found"));
                    order.setProductVariant(productVariant);
                    SaleDTO saleDTO = new SaleDTO(true, productVariant.getId(), 10, user.getId());
                    saleController.doSale(saleDTO);
                } else {
                    Product product = productRepository.findById(deskId)
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    order.setProduct(product);
                    SaleDTO saleDTO = new SaleDTO(false, product.getId(), 10, user.getId());
                    saleController.doSale(saleDTO);
                }
                order.setTime(TimeController.getLocalDateTime());

                orderRepository.save(order);
            }
        }

        // Config
        SystemConfig config = new SystemConfig();
        config.setBatePapoAtivo(true);
        config.setNomeEstabelecimento("Meu estabelecimento");
        config.setNumMesas(1000);
        config.setId(1L);
        configRepository.save(config);
    }
}
