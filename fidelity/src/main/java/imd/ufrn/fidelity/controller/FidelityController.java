package imd.ufrn.fidelity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import imd.ufrn.fidelity.model.BonusRequest;
import imd.ufrn.fidelity.service.FidelityService;

@Controller
public class FidelityController {
    @Autowired
    private FidelityService fidelityService;

    @PostMapping("/bonus")
    public void createBonus(@RequestBody BonusRequest bonusRequest) {
        fidelityService.createBonus(bonusRequest);
    }

}
