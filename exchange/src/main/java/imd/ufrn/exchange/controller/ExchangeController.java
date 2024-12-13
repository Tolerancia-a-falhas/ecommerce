package imd.ufrn.exchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import imd.ufrn.exchange.model.ExchangeResponse;
import imd.ufrn.exchange.service.ExchangeService;

@Controller
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/exchange")

    public ExchangeResponse getExchangeRate() {
        return exchangeService.getExchangeRate();
    }

}
