package com.radja.aggregatormicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AggregatedResultDto {
    private HumidityReadingDto humidityReading;
    private TemperatureReadingDto temperatureReading;


    @Override
    public String toString() {
        return "AggregatedResultDto{" +
                "humidityReading=" + humidityReading.toString() +
                ", temperatureReading=" + temperatureReading.toString() +
                '}';
    }
}
