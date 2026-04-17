package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.domain.CategorieBautura;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileOrderRepository;
import drinkshop.repository.file.FileProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderServiceTest {

    private OrderService orderService;
    private FileProductRepository productRepo;
    private FileOrderRepository orderRepo;
    private File productFile;
    private File orderFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        productFile = tempDir.resolve("test_products_wbt.txt").toFile();
        orderFile = tempDir.resolve("test_orders_wbt.txt").toFile();

        productFile.createNewFile();
        orderFile.createNewFile();

        try (FileWriter fw = new FileWriter(productFile)) {
            fw.write("1,Ceai,10.0,TEA,WATER_BASED\n");
            fw.write("2,Cafea,15.0,CLASSIC_COFFEE,BASIC\n");
        }

        productRepo = new FileProductRepository(productFile.getAbsolutePath());
        orderRepo = new FileOrderRepository(orderFile.getAbsolutePath(), productRepo);

        orderService = new OrderService(orderRepo, productRepo);
    }

    @AfterEach
    void tearDown() {
        if (productFile.exists()) productFile.delete();
        if (orderFile.exists()) orderFile.delete();
    }

    @Test
    @DisplayName("Test - Order Null - Path 1")
    void testComputeTotal_NullOrder() {
        double total = orderService.computeTotal(null);
        assertEquals(0.0, total, 0.001);
    }

    @Test
    @DisplayName("Test - Order cu lista de iteme null - Path 2")
    void testComputeTotal_NullItems() {
        Order o = new Order(10);
        o.setItems(null);
        double total = orderService.computeTotal(o);
        assertEquals(0.0, total, 0.001);
    }

    @Test
    @DisplayName("Test - Order cu lista de iteme vida - Path 3")
    void testComputeTotal_EmptyItems() {
        Order o = new Order(20);
        o.setItems(new ArrayList<>());
        double total = orderService.computeTotal(o);
        assertEquals(0.0, total, 0.001);
    }

    @Test
    @DisplayName("Test - Cantitate <= 0 - Path 4")
    void testComputeTotal_QuantityZero() {
        Order o = new Order(40);
        Product existingProduct = productRepo.findOne(1);
        o.getItems().add(new OrderItem(existingProduct, -1));

        double total = orderService.computeTotal(o);
        // Cantitatea e -1, dar functia returneaza doar cand cantitatea e > 0
        assertEquals(0.0, total, 0.001);
    }

    @Test
    @DisplayName("Test - Produs valid cantitate > 0 - Path 5")
    void testComputeTotal_ValidItem() {
        Order o = new Order(50);
        Product existingProduct = productRepo.findOne(1); // 10.0
        o.getItems().add(new OrderItem(existingProduct, 5));

        double total = orderService.computeTotal(o);
        assertEquals(50.0, total, 0.001); // 5 * 10
    }

    @Test
    @DisplayName("Test - Mai multe produse in bucla (Multiple Iterations)")
    void testComputeTotal_MultipleItems() {
        Order o = new Order(60);
        Product p1 = productRepo.findOne(1); // 10.0
        Product p2 = productRepo.findOne(2); // 15.0

        o.getItems().add(new OrderItem(p1, 2)); // 20.0
        o.getItems().add(new OrderItem(p2, 3)); // 45.0

        double total = orderService.computeTotal(o);
        assertEquals(65.0, total, 0.001);
    }
}
