package imd.ufrn.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private Long transactionId;
    private Long productId;
}
