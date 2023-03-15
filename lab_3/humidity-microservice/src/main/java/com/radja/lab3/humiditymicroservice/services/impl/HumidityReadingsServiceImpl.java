package com.radja.lab3.humiditymicroservice.services.impl;

import com.radja.lab3.humiditymicroservice.dto.HumidityReadingDto;
import com.radja.lab3.humiditymicroservice.model.HumidityReading;
import com.radja.lab3.humiditymicroservice.repository.HumidityReadingsRepository;
import com.radja.lab3.humiditymicroservice.services.HumidityReadingsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HumidityReadingsServiceImpl implements HumidityReadingsService {
    private final HumidityReadingsRepository humidityReadingsRepository;


    @Override
    public void saveReading(String unit, Integer value) {
        HumidityReading hr = HumidityReading.builder()
                .unit(unit)
                .value(value)
                .build();
        humidityReadingsRepository.save(hr);
    }

    @Override
    public HumidityReadingDto getByRow(long row) {
        HumidityReading hr = humidityReadingsRepository.getReferenceById(row);

        return HumidityReadingDto.builder()
                .unit(hr.getUnit())
                .value(hr.getValue())
                .name("Humidity")
                .build();
    }
}
