package imd.ufrn.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BonusRequest {
    // id do usuário que está executando a compra
    private Long user;

    // um valor inteiro mais próximo do valor do produto antes da conversão
    private Long bonus;
}
