package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


class ProductServiceTest {
    private ProductService productService;
    private File testFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        testFile = tempDir.resolve("test_products_advanced.txt").toFile();
        testFile.createNewFile();

        FileProductRepository repo = new FileProductRepository(testFile.getAbsolutePath());
        productService = new ProductService(repo);
    }

    @AfterEach
    void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Nested
    @Order(1)
    class ECPTests {

        @Test
        void testAddProduct_ECP_Valid() {
            Product p = new Product(10, "Espresso", 50.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

            productService.addProduct(p);

            Product saved = productService.findById(10);
            assertNotNull(saved);
            assertEquals("Espresso", saved.getNume());
            assertEquals(50.0, saved.getPret());
        }

        @Test
        void testAddProduct_ECP_InvalidPretHuge() {
            Product p = new Product(11, "Americano", 200.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

            ValidationException ex = assertThrows(ValidationException.class, () -> productService.addProduct(p));
            assertTrue(ex.getMessage().contains("Pretul trebuie sa fie intre 1.0 si 150.0"));
        }

        @Test
        void testAddProduct_ECP_InvalidNumeShort() {
            Product p = new Product(12, "Ce", 25.0, CategorieBautura.TEA, TipBautura.WATER_BASED);

            ValidationException ex = assertThrows(ValidationException.class, () -> productService.addProduct(p));
            assertTrue(ex.getMessage().contains("Numele trebuie sa aiba intre 3 si 30 caractere"));
        }

        @Test
        void testAddProduct_ECP_InvalidAmbele() {
            String veryLongName = "BauturaSpecialaCuUnNumeExtremDeLungPentruTest";
            double verySmallPrice = 0.5;
            Product p = new Product(13, veryLongName, verySmallPrice, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);

            ValidationException ex = assertThrows(ValidationException.class, () -> productService.addProduct(p));
            assertTrue(ex.getMessage().contains("Numele"));
            assertTrue(ex.getMessage().contains("Pretul"));
        }
    }

    @Nested
    @Order(2)
    class BVATests {

        @Test
        void testAddProduct_BVA_ValidMinPrice() {
            Product p = new Product(20, "Cafea Min", 1.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

            productService.addProduct(p);

            assertNotNull(productService.findById(20));
            assertEquals(1.0, productService.findById(20).getPret());
        }

        @Test
        void testAddProduct_BVA_ValidMaxPrice() {
            Product p = new Product(21, "Cafea Premium", 150.0, CategorieBautura.SPECIAL_COFFEE, TipBautura.ALL);

            productService.addProduct(p);

            assertNotNull(productService.findById(21));
            assertEquals(150.0, productService.findById(21).getPret());
        }

        @Test
        void testAddProduct_BVA_InvalidJustBelowMinPrice() {
            Product p = new Product(22, "SubLimita", 0.99, CategorieBautura.JUICE, TipBautura.WATER_BASED);

            ValidationException ex = assertThrows(ValidationException.class, () -> productService.addProduct(p));
            assertTrue(ex.getMessage().contains("Pretul trebuie"));
        }

        @Test
        void testAddProduct_BVA_InvalidJustAboveMaxPrice() {
            Product p = new Product(23, "Peste Limita", 150.01, CategorieBautura.SMOOTHIE, TipBautura.PLANT_BASED);

            ValidationException ex = assertThrows(ValidationException.class, () -> productService.addProduct(p));
            assertTrue(ex.getMessage().contains("Pretul trebuie"));
        }
    }
}

