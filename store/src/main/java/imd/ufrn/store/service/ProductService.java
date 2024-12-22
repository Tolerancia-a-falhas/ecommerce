package imd.ufrn.store.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import imd.ufrn.store.model.ProductResponse;
import imd.ufrn.store.model.SellResponse;
import imd.ufrn.store.model.Transaction;

@Service
public class ProductService {
    private List<Transaction> soldProductsLog = new ArrayList<>();
    private Long nextTransactionId = 1L;

    private static final double AMPLITUDE = 4.0;
    private static final double FREQUENCY = 0.6;
    private static final double BASE = 5.0;

    // 2 -> 2 ok before failure
    private static Random randomError = new Random(2);
    private static Random randomTransctionId = new Random();
    private static int timeMinOmission = 5;

    private Instant lastError = Instant.MIN;
    private Duration errDuration = Duration.ofSeconds(5);

    public ProductResponse getProduct(Long id) {
        simulateProductOmission();

        return generateDeterministicProductById(id);
    }

    public SellResponse sellProduct(Long id) {
        simulateSellError();

        Long transactionId = Long.parseLong(
                String.valueOf(nextTransactionId) + "000" + randomTransctionId.nextLong(99999, 99999999));
        nextTransactionId++;
        soldProductsLog.add(new Transaction(transactionId, id));
        return new SellResponse(transactionId);
    }

    private ProductResponse generateDeterministicProductById(Long id) {
        double value = BASE + AMPLITUDE * Math.sin(FREQUENCY * id);
        String name = "productName " + id.toString();

        return new ProductResponse(id, name, value);
    }

    private void simulateProductOmission() {
        if (isInErrorState()) {
            executeError();
        } else {
            int chance = randomError.nextInt(10);
            if (chance <= 1) {
                System.out.println("ERROR: store get product omission");
                sleepMinutes(timeMinOmission);
            }
        }
    }

    private void simulateSellError() {
        if (isInErrorState()) {
            executeError();
        } else {
            int chance = randomError.nextInt(10);
            if (chance == 0) {
                setStartErrorState();
                executeError();
            }
        }
    }

    private void executeError() {
        System.out.println("ERROR: sell product time error");
        throw new Error();
    }

    private Boolean isInErrorState() {
        Instant now = Instant.now();
        Duration timeSinceLastError = Duration.between(lastError, now);

        if (timeSinceLastError.compareTo(errDuration) <= 0) {
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
