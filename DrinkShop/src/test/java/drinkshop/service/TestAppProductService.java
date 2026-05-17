package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

public class TestAppProductService {

    private final Repository<Integer, Product> productRepo;
    private final Validator<Product> productValidator;

    public TestAppProductService(Repository<Integer, Product> productRepo, Validator<Product> productValidator) {
        this.productRepo = productRepo;
        this.productValidator = productValidator;
    }

    public void addProduct(Product p) {
        productValidator.validate(p);
        productRepo.save(p);
    }

    public Product findById(int id) {
        return productRepo.findOne(id);
    }
}
