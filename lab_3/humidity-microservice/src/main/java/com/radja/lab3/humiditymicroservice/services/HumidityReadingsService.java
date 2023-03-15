package com.radja.lab3.humiditymicroservice.services;

import com.radja.lab3.humiditymicroservice.dto.HumidityReadingDto;
import com.radja.lab3.humiditymicroservice.model.HumidityReading;
import org.springframework.stereotype.Component;

@Component
public interface HumidityReadingsService {
    void saveReading(String unit, Integer value);

    HumidityReadingDto getByRow(long row);
}
