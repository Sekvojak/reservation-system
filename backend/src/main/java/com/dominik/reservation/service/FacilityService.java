package com.dominik.reservation.service;

import com.dominik.reservation.dto.FacilityCreateRequest;
import com.dominik.reservation.dto.FacilityResponse;
import com.dominik.reservation.entity.Facility;
import com.dominik.reservation.exception.ConflictException;
import com.dominik.reservation.exception.NotFoundException;
import com.dominik.reservation.repository.FacilityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public FacilityResponse create(FacilityCreateRequest request) {
        if (facilityRepository.existsByName(request.name())) {
            throw new ConflictException("Facility name already exists");
        }

        Facility facility = new Facility();
        facility.setName(request.name());
        facility.setType(request.type());

        Facility saved = facilityRepository.save(facility);
        return toResponse(saved);
    }

    public Page<FacilityResponse> list(Pageable pageable) {
        return facilityRepository.findAll(pageable).map(this::toResponse);
    }

    private FacilityResponse toResponse(Facility facility) {
        return new FacilityResponse(
                facility.getId(),
                facility.getName(),
                facility.getType(),
                facility.getCreatedAt()
        );
    }


    public FacilityResponse getById(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Facility not found"));

        return toResponse(facility);
    }

    public void delete(Long id) {
        if (!facilityRepository.existsById(id)) {
            throw new NotFoundException("Facility not found");
        }
        facilityRepository.deleteById(id);
    }


}
