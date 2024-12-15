package imd.ufrn.exchange.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import imd.ufrn.exchange.model.ExchangeResponse;

@Service
public class ExchangeService {
    private static final double AMPLITUDE = 4.0;
    private static final double FREQUENCY = 0.3;
    private static final double BASE = 5.0;
    private int timer = 1;
    // 9 -> 31 before failure; 8 -> 2 before failure
    private static Random random = new Random(8);

    @Autowired
    private ApplicationTerminator terminator;

    public ExchangeResponse getExchangeRate() {
        simulateFailure();

        return new ExchangeResponse(generateNextRate());
    }

    private double generateNextRate() {
        double rate = BASE + AMPLITUDE * Math.sin(FREQUENCY * timer);
        timer++;
        return rate;
    }

    public void simulateFailure() throws Error {
        int chance = random.nextInt(10);

        if (chance == 0) {
            terminator.stopApplication();
        }
    }

}
