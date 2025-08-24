package com.example.server.controller;

import com.example.server.dto.req.AddressRequest;
import com.example.server.dto.res.ApiResponse;
import com.example.server.entity.Address;
import com.example.server.service.ShopAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/address")
@RequiredArgsConstructor
public class ShopAddressController {

    private final ShopAddressService addressService;

    @GetMapping("/get/{userId}")
    public ApiResponse<List<Address>> getAddresses(@PathVariable String userId) {
        return addressService.getAddresses(userId);
    }

    @PostMapping("/add")
    public ApiResponse<Address> addAddress(@RequestBody AddressRequest request) {
        return addressService.addAddress(request);
    }

    @PutMapping("/update/{userId}/{addressId}")
    public ApiResponse<Address> updateAddress(
            @PathVariable String userId,
            @PathVariable String addressId,
            @RequestBody AddressRequest request) {
        return addressService.updateAddress(userId, addressId, request);
    }

    @DeleteMapping("/delete/{userId}/{addressId}")
    public ApiResponse<String> deleteAddress(
            @PathVariable String userId,
            @PathVariable String addressId) {
        return addressService.deleteAddress(userId, addressId);
    }
}
