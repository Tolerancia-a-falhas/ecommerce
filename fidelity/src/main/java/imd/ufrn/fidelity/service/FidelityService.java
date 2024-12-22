package imd.ufrn.fidelity.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import imd.ufrn.fidelity.model.BonusRequest;

@Service
public class FidelityService {
    private static Random random = new Random();
    private Duration timeSleepDuringError = Duration.ofSeconds(2);

    private List<BonusRequest> bonusHistory = new ArrayList<>();

    private Instant lastError = Instant.MIN;
    private Duration errDuration = Duration.ofSeconds(30);

    public void createBonus(BonusRequest bonusRequest) {
        simulateError();

        bonusHistory.add(bonusRequest);
    }

    private void simulateError() {
        if (isInErrorState()) {
            executeError();
        }
        int chance = random.nextInt(10);
        if (chance == 0) {
            setStartErrorState();
            executeError();
        }
    }

    private void executeError() {
        System.out.println("Error: fidelity sleep");
        sleepMilis((int) timeSleepDuringError.toMillis());
    }

    private void sleepMilis(int milis) {
        try {
            Thread.sleep(milis);
        } catch (Exception e) {
        }
    }

    private void setStartErrorState() {
        lastError = Instant.now();
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

}
