package com.radja.temperaturemicroservice.repository;

import com.radja.temperaturemicroservice.model.TemperatureReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureReadingsRepository extends JpaRepository<TemperatureReading, Long> {
}
