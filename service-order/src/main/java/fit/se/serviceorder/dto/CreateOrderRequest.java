package fit.se.serviceorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private Long userId;
    private String deliveryAddress;
    private String phoneNumber;
    private String notes;
    private List<OrderItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        private Long foodId;
        private String foodName;
        private Double price;
        private Integer quantity;
        private String notes;
    }
}
