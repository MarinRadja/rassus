package com.radja.temperaturemicroservice.services;

import com.radja.temperaturemicroservice.dto.TemperatureReadingDto;
import org.springframework.stereotype.Component;

@Component
public interface TemperatureReadingsService {
    void saveReading(String unit, Integer value);

    TemperatureReadingDto getByRow(long row);
}
