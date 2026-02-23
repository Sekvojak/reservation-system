package com.dominik.reservation.controller;

import com.dominik.reservation.dto.FacilityCreateRequest;
import com.dominik.reservation.dto.FacilityResponse;
import com.dominik.reservation.service.FacilityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @PostMapping
    public FacilityResponse create(@Valid @RequestBody FacilityCreateRequest request) {
        return facilityService.create(request);
    }

    @GetMapping
    public Page<FacilityResponse> list(Pageable pageable) {
        return  facilityService.list(pageable);
    }

    @GetMapping("/{id}")
    public FacilityResponse getById(@PathVariable Long id) {
        return facilityService.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
