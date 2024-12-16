package imd.ufrn.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private double value;
}