package imd.ufrn.exchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import imd.ufrn.exchange.model.ExchangeResponse;
import imd.ufrn.exchange.service.ExchangeService;

@RestController
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/exchange")

    public ExchangeResponse getExchangeRate() {
        System.out.println("controller exchange");
        return exchangeService.getExchangeRate();
    }

}
