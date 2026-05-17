package drinkshop.service;
import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
public class Step1UnitMockTest {
    private TestAppProductService serviceS;
    private Repository<Integer, Product> mockRepoR;
    private Validator<Product> mockValidatorV;
    @BeforeEach
    void setUp() {
        mockRepoR = (Repository<Integer, Product>) mock(Repository.class);
        mockValidatorV = (Validator<Product>) mock(Validator.class);
        serviceS = new TestAppProductService(mockRepoR, mockValidatorV);
    }
    @Test
    void testAddProduct_Success() {
        Product E = new Product(10, "Limonada", 20.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        doNothing().when(mockValidatorV).validate(E);
        when(mockRepoR.save(E)).thenReturn(E);
        serviceS.addProduct(E);
        verify(mockValidatorV, times(1)).validate(E);
        verify(mockRepoR, times(1)).save(E);
    }
    @Test
    void testFindById() {
        Product E = new Product(20, "Americano", 15.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);
        when(mockRepoR.findOne(20)).thenReturn(E);
        Product result = serviceS.findById(20);
        assertNotNull(result);
        assertEquals("Americano", result.getNume());
        verify(mockRepoR, times(1)).findOne(20);
    }
}
