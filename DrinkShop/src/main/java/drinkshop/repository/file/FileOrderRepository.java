package drinkshop.repository.file;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class FileOrderRepository
        extends FileAbstractRepository<Integer, Order> {

    private Repository<Integer, Product> productRepository;

    public FileOrderRepository(String fileName, Repository<Integer, Product> productRepository) {
        super(fileName);
        this.productRepository = productRepository;
        loadFromFile();
    }

    @Override
    protected Integer getId(Order entity) {
        return entity.getId();
    }

    @Override
    protected Order extractEntity(String line) {
        // Format: id,products,total
        // products: id:qty|id:qty
        String[] parts = line.split(",");
        if (parts.length < 3) {
            // Log error or ignore
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0]);

            List<OrderItem> items = new ArrayList<>();
            // Check if parts[1] is not empty
            if (!parts[1].isEmpty()) {
                String[] products = parts[1].split("\\|");

                for (String product : products) {
                    String[] prodParts = product.split(":");
                    if (prodParts.length < 2) continue;

                    int productId = Integer.parseInt(prodParts[0]);
                    int quantity = Integer.parseInt(prodParts[1]);

                    Product p = productRepository.findOne(productId);
                    if (p != null) {
                        items.add(new OrderItem(p, quantity));
                    }
                }
            }

            double totalPrice = Double.parseDouble(parts[2]);

            return new Order(id, items, totalPrice);
        } catch (NumberFormatException e) {
            // Log parse error
            return null;
        }
    }

    @Override
    protected String createEntityAsString(Order entity) {

        StringBuilder sb = new StringBuilder();

        for (OrderItem item : entity.getItems()) {

            if (sb.length() > 0) {
                sb.append("|");
            }

            sb.append(item.getProduct().getId())
                    .append(":")
                    .append(item.getQuantity());
        }

        return entity.getId() + "," +
                sb + "," +
                entity.getTotalPrice();
    }
}
