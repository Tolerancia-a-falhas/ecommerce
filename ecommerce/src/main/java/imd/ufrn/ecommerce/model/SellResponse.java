package imd.ufrn.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellResponse {
    // um id único da transação (gerado automaticamente) que representa essa venda.
    private Long transactionId;
}
