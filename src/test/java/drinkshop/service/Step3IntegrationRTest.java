package drinkshop.service;
import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * Step 3. Integrare R (Se testează S + V impreuna cu R real) 
 * Nu folosim mock-uri pentru niciun modul S, V sau R.
 */
public class Step3IntegrationRTest {
    private TestAppProductService serviceS;
    private FileProductRepository realRepoR;
    private ProductValidator realValidatorV;
    private File testFile;
    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        testFile = tempDir.resolve("test_integration_repo.txt").toFile();
        testFile.createNewFile();
        realRepoR = new FileProductRepository(testFile.getAbsolutePath());
        realValidatorV = new ProductValidator();
        serviceS = new TestAppProductService(realRepoR, realValidatorV);
    }
    @AfterEach
    void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    @Test
    void testAddProductAndRead_Valid() {
        Product E = new Product(40, "Caffe Latte", 25.0, CategorieBautura.MILK_COFFEE, TipBautura.BASIC);
        serviceS.addProduct(E);
        Product saved = serviceS.findById(40);
        assertNotNull(saved);
        assertEquals("Caffe Latte", saved.getNume());
        assertEquals(25.0, saved.getPret());
        assertEquals(CategorieBautura.MILK_COFFEE, saved.getCategorie());
    }
    @Test
    void testAddProduct_ValidationError() {
        Product E = new Product(41, "Caffe Latte", -10.0, CategorieBautura.MILK_COFFEE, TipBautura.BASIC);
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            serviceS.addProduct(E);
        });
        assertTrue(thrown.getMessage().contains("Pretul trebuie sa fie"));
    }
}
