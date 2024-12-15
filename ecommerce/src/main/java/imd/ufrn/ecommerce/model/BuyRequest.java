package imd.ufrn.ecommerce.model;

import lombok.Data;

@Data
public class BuyRequest {
    // id do produto a ser comprado
    private Long product;
    // id do usuário que está executando a compra
    private Long user;
    // parâmetro que vai indicar se a tolerância a falhas está ativada ou não (true
    // ou false)
    private Boolean ft;
}
