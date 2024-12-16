package imd.ufrn.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import imd.ufrn.store.model.ProductResponse;
import imd.ufrn.store.model.SellResponse;
import imd.ufrn.store.service.ProductService;

@RestController
public class StoreController {
    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping("/sell/{id}")
    public SellResponse sellProduct(@PathVariable Long id) {
        return productService.sellProduct(id);

    }

}
