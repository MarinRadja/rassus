package com.radja.aggregatormicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TemperatureReadingDto {;
    private String name;
    private String unit;
    private Integer value;

    @Override
    public String toString() {
        return "TemperatureReadingDto{" +
                "name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}