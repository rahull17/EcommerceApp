package com.example.server.service;

import com.example.server.dto.req.AddressRequest;
import com.example.server.dto.res.ApiResponse;
import com.example.server.entity.Address;
import com.example.server.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopAddressService {

    private final AddressRepository addressRepository;

    public ApiResponse<List<Address>> getAddresses(String userId) {
        try {
            List<Address> addresses = addressRepository.findByUserId(userId);
            return new ApiResponse<>(true, "Addresses fetched successfully", addresses);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<Address> addAddress(AddressRequest request) {
        try {
            Address address = Address.builder()
                    .userId(request.getUserId())
                    .address(request.getAddress())
                    .city(request.getCity())
                    .pincode(request.getPincode())
                    .phone(request.getPhone())
                    .notes(request.getNotes())
                    .build();

            Address saved = addressRepository.save(address);
            return new ApiResponse<>(true, "Address added successfully", saved);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: " + e.getMessage(), null);
        }
    }

    // Update address
    public ApiResponse<Address> updateAddress(String userId, String addressId, AddressRequest request) {
        try {
            return addressRepository.findById(addressId)
                    .map(existing -> {
                        if (!existing.getUserId().equals(userId)) {
                            return new ApiResponse<Address>(false, "Unauthorized update attempt", null);
                        }
                        existing.setAddress(request.getAddress());
                        existing.setCity(request.getCity());
                        existing.setPincode(request.getPincode());
                        existing.setPhone(request.getPhone());
                        existing.setNotes(request.getNotes());
                        Address updated = addressRepository.save(existing);
                        return new ApiResponse<>(true, "Address updated successfully", updated);
                    })
                    .orElseGet(() -> new ApiResponse<Address>(false, "Address not found", null)); // 👈 explicit type
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: " + e.getMessage(), null);
        }
    }


    public ApiResponse<String> deleteAddress(String userId, String addressId) {
        try {
            return addressRepository.findById(addressId)
                    .map(existing -> {
                        if (!existing.getUserId().equals(userId)) {
                            return new ApiResponse<String>(false, "Unauthorized delete attempt", null);
                        }
                        addressRepository.delete(existing);
                        return new ApiResponse<String>(true, "Address deleted successfully", null);
                    })
                    .orElseGet(() -> new ApiResponse<String>(false, "Address not found", null)); // 👈 explicit type
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: " + e.getMessage(), null);
        }
    }
}
