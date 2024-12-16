package imd.ufrn.ecommerce.service;

import java.net.URI;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import imd.ufrn.ecommerce.model.BonusRequest;
import imd.ufrn.ecommerce.model.BuyRequest;
import imd.ufrn.ecommerce.model.BuyResponse;
import imd.ufrn.ecommerce.model.ExchangeResponse;
import imd.ufrn.ecommerce.model.ProductResponse;
import imd.ufrn.ecommerce.model.SellResponse;

@Service
public class EcommerceService {

    @Autowired
    private WebClient webClient;

    // Base URLs for the other applications
    private static final String EXCHANGEBASEURL = "http://exchange-service/api";
    private static final String FIDELITYBASEURL = "http://fidelity-service/api";
    private static final String STOREBASEURL = "http://store-service/api";

    public BuyResponse createBuy(BuyRequest buyRequest) {
        Long productId = buyRequest.getProduct();
        Long userId = buyRequest.getUser();
        boolean faultTolerance = buyRequest.getFt();

        // Request 1: Call Store to get product details
        ProductResponse productResponse = this.fetchProductDetails(productId).getBody();

        // Request 2: Call Exchange to get the conversion rate
        double exchangeRate = this.fetchExchangeRate().getBody().getRate();

        double convertedPrice = productResponse.getValue() * exchangeRate;

        // Request 3: Call Store to complete the sale
        Long transactionId = this.callSellProduct(productId).getBody().getTransactionId();

        // Request 4: Call Fidelity to add bonus points
        Long bonusPoints = Math.round(productResponse.getValue());
        this.sendBonus(userId, bonusPoints);

        return new BuyResponse(transactionId);
    }

    private ResponseEntity<ExchangeResponse> fetchExchangeRate() {
        URI exchangeUri = URI.create(EXCHANGEBASEURL + "/exchange");
        return webClient.get()
                .uri(exchangeUri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ExchangeResponse.class)
                .block();
        // return restTemplate.getForEntity(exchangeUri, ExchangeResponse.class);
    }

    private ResponseEntity<Void> sendBonus(Long userId, Long bonus) {
        BonusRequest bonusRequest = new BonusRequest(userId, bonus);
        URI bonusUri = URI.create(FIDELITYBASEURL + "/bonus");

        return webClient.post()
                .uri(bonusUri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bonusRequest)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Void.class)
                .block();
        // return restTemplate.postForEntity(bonusUri, bonusRequest, Void.class);
    }

    private ResponseEntity<ProductResponse> fetchProductDetails(Long productId) {
        URI getProductUri = URI.create(STOREBASEURL + "/product/" + productId);
        return webClient.get()
                .uri(getProductUri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ProductResponse.class)
                .block();
        // return restTemplate.getForEntity(getProductUri, ProductResponse.class);
    }

    private ResponseEntity<SellResponse> callSellProduct(Long productId) {
        URI sellProductUri = URI.create(STOREBASEURL + "/sell/" + productId);

        // TODO: post body ausence is ok?
        return webClient.post()
                .uri(sellProductUri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(null)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(SellResponse.class)
                .block();
        // return restTemplate.postForEntity(sellProductUri, null, SellResponse.class);
    }

}
