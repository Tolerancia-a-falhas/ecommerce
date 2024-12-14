package imd.ufrn.fidelity.model;

import lombok.Data;

@Data
public class BonusRequest {
    // id do usuário que está executando a compra
    private Long user;

    // um valor inteiro mais próximo do valor do produto antes da conversão
    private Long bonus;
}
