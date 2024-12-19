package imd.ufrn.fidelity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import imd.ufrn.fidelity.model.BonusRequest;
import imd.ufrn.fidelity.service.FidelityService;

@RestController
public class FidelityController {
    @Autowired
    private FidelityService fidelityService;

    @PostMapping("/bonus")
    public void createBonus(@RequestBody BonusRequest bonusRequest) {
        System.out.println("controller fidelity bonus");
        fidelityService.createBonus(bonusRequest);
    }

}
