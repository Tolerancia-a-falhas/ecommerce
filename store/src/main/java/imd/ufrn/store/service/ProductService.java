package imd.ufrn.store.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private static Random random = new Random();
    private static int timeMinOmission = 10;

    private Instant lastError = Instant.MIN;
    private Duration errDurationInMin = Duration.ofSeconds(5);

    public Product getProduct(Long id) {
        simulateProductOmission();

        return generateDeterministicProductById(id);
    }

    public SellResponse sellProduct(Long id) {
        if (simulateSellError()) {
            throw new Error();
        }

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

    private void simulateProductOmission() {
        int chance = random.nextInt(10);
        if (chance <= 1) {
            sleepMinutes(timeMinOmission);
        }
    }

    private Boolean simulateSellError() {
        if (isInErrorState()) {
            return true;
        }

        int chance = random.nextInt(10);
        if (chance == 0) {
            setStartErrorState();
            return true;
        }

        return false;
    }

    private Boolean isInErrorState() {
        Instant now = Instant.now();
        Duration timeSinceLastError = Duration.between(lastError, now);

        if (timeSinceLastError.compareTo(errDurationInMin) <= 0) {
            return true;
        } else {
            return false;
        }

    }

    private void setStartErrorState() {
        lastError = Instant.now();
    }

    private void sleepMinutes(int minutes) {
        int counter = 0;
        while (counter < minutes) {
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
            }
            counter++;
        }
    }

}
