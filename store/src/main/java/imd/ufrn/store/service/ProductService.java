package imd.ufrn.store.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import imd.ufrn.store.model.Product;
import imd.ufrn.store.model.SellResponse;
import imd.ufrn.store.model.Transaction;

@Service
public class ProductService {
    private List<Transaction> soldProductsLog = new ArrayList<>();
    private Long nextTransactionId = 1L;

    private static final double AMPLITUDE = 4.0;
    private static final double FREQUENCY = 0.6;
    private static final double BASE = 5.0;

    public Product getProduct(Long id) {
        return generateDeterministicProductById(id);
    }

    public SellResponse sellProduct(Long id) {
        Long transactionId = nextTransactionId;
        nextTransactionId++;
        soldProductsLog.add(new Transaction(transactionId, id));
        return new SellResponse(transactionId);
    }

    private Product generateDeterministicProductById(Long id) {
        double value = BASE + AMPLITUDE * Math.sin(FREQUENCY * id);
        String name = "productName " + id.toString();

        return new Product(id, name, value);
    }

}
