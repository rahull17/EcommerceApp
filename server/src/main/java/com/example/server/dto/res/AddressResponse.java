package com.example.server.dto.res;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private String id;
    private String userId;
    private String address;
    private String city;
    private String pincode;
    private String phone;
    private String notes;
}
