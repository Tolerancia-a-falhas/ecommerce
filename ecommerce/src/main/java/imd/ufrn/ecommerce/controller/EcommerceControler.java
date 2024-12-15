package imd.ufrn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import imd.ufrn.ecommerce.model.BuyRequest;
import imd.ufrn.ecommerce.model.BuyResponse;
import imd.ufrn.ecommerce.service.EcommerceService;

@Controller
public class EcommerceControler {
    @Autowired
    private EcommerceService ecommerceService;

    @PostMapping("/buy")
    public BuyResponse createBuy(@RequestBody BuyRequest buy) {
        return ecommerceService.createBuy(buy);
    }

}
