package com.radja.temperaturemicroservice.dto;

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
}
