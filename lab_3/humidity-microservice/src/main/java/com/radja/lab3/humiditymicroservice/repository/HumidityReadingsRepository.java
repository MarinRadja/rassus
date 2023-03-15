package com.radja.lab3.humiditymicroservice.repository;

import com.radja.lab3.humiditymicroservice.model.HumidityReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumidityReadingsRepository extends JpaRepository<HumidityReading, Long> {
}
