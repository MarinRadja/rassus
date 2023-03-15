package com.radja.lab3.humiditymicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HumidityReadingDto {;
    private String name;
    private String unit;
    private Integer value;
}
