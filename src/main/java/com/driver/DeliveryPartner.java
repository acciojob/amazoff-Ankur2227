package com.driver;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryPartner {
    private String partnerId;
    private int numberOfOrders = 0;

    public DeliveryPartner(String partnerId) {
        this.partnerId = partnerId;
    }
}