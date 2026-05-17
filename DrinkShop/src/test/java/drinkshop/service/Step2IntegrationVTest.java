package drinkshop.service;
import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
/**
 * Step 2. Integrare V (Testare S impreună cu V real) 
 * Pentru R (Repository) se foloseste obiect mock.
 */
public class Step2IntegrationVTest {
    private TestAppProductService serviceS;
    private Repository<Integer, Product> mockRepoR;
    private ProductValidator realValidatorV;
    @BeforeEach
    void setUp() {
        mockRepoR = (Repository<Integer, Product>) mock(Repository.class);
        realValidatorV = new ProductValidator();
        serviceS = new TestAppProductService(mockRepoR, realValidatorV);
    }
    @Test
    void testAddProduct_Valid() {
        Product E = new Product(30, "Ceai Verde", 15.0, CategorieBautura.TEA, TipBautura.WATER_BASED);
        when(mockRepoR.save(E)).thenReturn(E);

        serviceS.addProduct(E);

        // Verificam doar repository ca totul e corect
        verify(mockRepoR, times(1)).save(E);
    }
    @Test
    void testAddProduct_InvalidName() {
        // Nume prea scurt
        Product E = new Product(31, "O", 15.0, CategorieBautura.TEA, TipBautura.WATER_BASED);

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            serviceS.addProduct(E);
        });
        assertTrue(ex.getMessage().contains("Numele trebuie sa aiba"));
        // Assert ca save nu a rulat
        verify(mockRepoR, never()).save(any(Product.class));
    }
}
