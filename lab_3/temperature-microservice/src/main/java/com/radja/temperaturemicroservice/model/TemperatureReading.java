package com.radja.temperaturemicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tempterature")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemperatureReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readingId;
    private String unit;
    private Integer value;

}
