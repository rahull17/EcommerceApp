package com.example.server.dto.req;

import lombok.Data;

@Data
public class AddressRequest {
    private String userId;
    private String address;
    private String city;
    private String pincode;
    private String phone;
    private String notes;
}
