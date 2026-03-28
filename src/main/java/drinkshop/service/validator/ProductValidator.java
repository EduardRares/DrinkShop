package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {

        String errors = "";

        if (product.getId() <= 0)
            errors += "ID invalid!\n";

        if (product.getNume() == null || product.getNume().length() < 3 || product.getNume().length() > 30)
            errors += "Numele trebuie sa aiba intre 3 si 30 caractere!\n";

        if (product.getPret() < 1.0 || product.getPret() > 150.0)
            errors += "Pretul trebuie sa fie intre 1.0 si 150.0 lei!\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
