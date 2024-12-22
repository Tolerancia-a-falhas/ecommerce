package imd.ufrn.ecommerce.service;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import imd.ufrn.ecommerce.model.BonusRequest;
import imd.ufrn.ecommerce.model.BuyRequest;
import imd.ufrn.ecommerce.model.BuyResponse;
import imd.ufrn.ecommerce.model.ExchangeResponse;
import imd.ufrn.ecommerce.model.ProductResponse;
import imd.ufrn.ecommerce.model.SellResponse;
import reactor.util.retry.Retry;

@Service
public class EcommerceService {

    @Autowired
    private WebClient webClient;

    // Base URLs for the other applications
    private static final String EXCHANGEBASEURL = "http://exchange:8081";
    private static final String FIDELITYBASEURL = "http://fidelity:8082";
    private static final String STOREBASEURL = "http://store:8083";

    private Double lastExchange = null;

    private List<BonusRequest> fidelityWaitQueue = new ArrayList();

    public BuyResponse createBuy(BuyRequest buyRequest) {
        Long productId = buyRequest.getProduct();
        Long userId = buyRequest.getUser();
        boolean faultTolerance = buyRequest.getFt();

        // Request 1: Call Store to get product details
        ProductResponse productResponse = this.fetchProductDetails(productId, faultTolerance).getBody();

        // Request 2: Call Exchange to get the conversion rate
        double exchangeRate = this.getExchangeRate(faultTolerance);

        double convertedPrice = productResponse.getValue() * exchangeRate;

        // Request 3: Call Store to complete the sale
        Long transactionId = this.callSellProduct(productId, faultTolerance).getBody().getTransactionId();

        // Request 4: Call Fidelity to add bonus points
        Long bonusPoints = Math.round(productResponse.getValue());
        this.sendBonus(userId, bonusPoints, faultTolerance);

        return new BuyResponse(transactionId);
    }

    private double getExchangeRate(boolean faultTolerance) {
        try {
            double rate = fetchExchangeRate(faultTolerance).getBody().getRate();
            lastExchange = rate;
            return rate;
        } catch (Exception e) {
            System.out.println("Failed to get Exchange Rate. Should try to tolerate failures: " + faultTolerance);
            if (faultTolerance && lastExchange != null) {
                System.out.println("Tolerated Exchange failure using cached value: " + lastExchange);
                return lastExchange;
            } else {
                throw new Error("Exchange failed to provide a value, and there is no value cached");
            }
        }
    }

    private void sendBonus(Long userId, Long bonus, boolean faultTolerance) {
        BonusRequest bonusRequest = new BonusRequest(userId, bonus);
        boolean requestWorked = true;
        try {
            sendBonusRequest(bonusRequest);
            requestWorked = true;
        } catch (Exception e) {
            requestWorked = false;
            System.out.println("Failed to register Bonus. Should try to tolerate failures: " + faultTolerance);
            if (faultTolerance) {
                fidelityWaitQueue.add(bonusRequest);
                System.out.println("added to wait queue bonus of user: " + userId + " with value of: " + bonus);
            }
        }
        if (requestWorked && faultTolerance && !fidelityWaitQueue.isEmpty()) {
            try {
                System.out.println("fidelity request worked. Trying to resend old bonuses");
                for (int i = 0; i < fidelityWaitQueue.size(); ++i) {
                    BonusRequest bonusRequestQueue = fidelityWaitQueue.get(i);
                    sendBonusRequest(bonusRequestQueue);
                    fidelityWaitQueue.remove(i);
                    i--;
                }
            } catch (Exception e) {
                System.out.println("fidelity failed while trying to send old bonuses. Normal operation continuing");
            }
        }
    }

    private ResponseEntity<ExchangeResponse> fetchExchangeRate(boolean faultTolerance) {
        URI exchangeUri = URI.create(EXCHANGEBASEURL + "/exchange");
        if (faultTolerance) {
            return webClient.get()
                    .uri(exchangeUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(ExchangeResponse.class)
                    .retryWhen(Retry.backoff(4, Duration.ofSeconds(2)))
                    .block();
        } else {
            return webClient.get()
                    .uri(exchangeUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(ExchangeResponse.class)
                    .block();
        }
    }

    private ResponseEntity<Void> sendBonusRequest(BonusRequest bonusRequest) {
        URI bonusUri = URI.create(FIDELITYBASEURL + "/bonus");

        return webClient.post()
                .uri(bonusUri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bonusRequest)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Void.class)
                .block();
    }

    private ResponseEntity<ProductResponse> fetchProductDetails(Long productId, boolean faultTolerance) {
        URI getProductUri = URI.create(STOREBASEURL + "/product/" + productId);

        if (faultTolerance) {
            return webClient.get()
                    .uri(getProductUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(ProductResponse.class)
                    .retryWhen(Retry.backoff(4, Duration.ofSeconds(2)))
                    .block();
        } else {
            return webClient.get()
                    .uri(getProductUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(ProductResponse.class)
                    .block();
        }
    }

    private ResponseEntity<SellResponse> callSellProduct(Long productId, boolean faultTolerance) {
        URI sellProductUri = URI.create(STOREBASEURL + "/sell/" + productId);

        if (faultTolerance) {
            return webClient.post()
                    .uri(sellProductUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(SellResponse.class)
                    .retryWhen(Retry.backoff(4, Duration.ofSeconds(2)))
                    .block();
        } else {
            return webClient.post()
                    .uri(sellProductUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(SellResponse.class)
                    .block();
        }
    }

}
