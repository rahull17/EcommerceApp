package com.example.server.controller;

import com.example.server.dto.res.ApiResponse;
import com.example.server.entity.Feature;
import com.example.server.service.FeatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/common/feature")
public class CommonFeatureController {

    private final FeatureService featureService;

    public CommonFeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    // Add a feature image (URL)
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Feature>> addFeatureImage(@RequestBody Feature feature) {
        Feature savedFeature = featureService.createFeature(feature);
        return ResponseEntity.ok(new ApiResponse<>(true, "Feature added", savedFeature));
    }

    // Get all feature images
    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<Feature>>> getFeatureImages() {
        List<Feature> features = featureService.getAllFeatures();
        return ResponseEntity.ok(new ApiResponse<>(true, "Features fetched", features));
    }
}
