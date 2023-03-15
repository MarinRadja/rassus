package com.radja.temperaturemicroservice.services.impl;

import com.radja.temperaturemicroservice.dto.TemperatureReadingDto;
import com.radja.temperaturemicroservice.model.TemperatureReading;
import com.radja.temperaturemicroservice.repository.TemperatureReadingsRepository;
import com.radja.temperaturemicroservice.services.TemperatureReadingsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TemperatureReadingsServiceImpl implements TemperatureReadingsService {
    private final TemperatureReadingsRepository temperatureReadingsRepository;


    @Override
    public void saveReading(String unit, Integer value) {
        TemperatureReading tr = TemperatureReading.builder()
                .unit(unit)
                .value(value)
                .build();
        temperatureReadingsRepository.save(tr);
    }

    @Override
    public TemperatureReadingDto getByRow(long row) {
        TemperatureReading hr = temperatureReadingsRepository.getReferenceById(row);

        return TemperatureReadingDto.builder()
                .unit(hr.getUnit())
                .value(hr.getValue())
                .name("Temperature")
                .build();
    }
}
