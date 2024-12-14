package imd.ufrn.fidelity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import imd.ufrn.fidelity.model.BonusRequest;

@Service
public class FidelityService {
    private static Random random = new Random();
    private int timeErrorMilis = 2000;

    private List<BonusRequest> bonusHistory = new ArrayList<>();

    public void createBonus(BonusRequest bonusRequest) {
        simulateError();

        bonusHistory.add(bonusRequest);
    }

    private void simulateError() {
        int chance = random.nextInt(10);
        if (chance == 0) {
            sleepMilis(timeErrorMilis);
        }
    }

    private void sleepMilis(int milis) {
        try {
            Thread.sleep(milis);
        } catch (Exception e) {
        }
    }

}
