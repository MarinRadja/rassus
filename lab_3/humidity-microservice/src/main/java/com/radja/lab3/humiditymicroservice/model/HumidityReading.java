package com.radja.lab3.humiditymicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "humidity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HumidityReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readingId;
    private String unit;
    private Integer value;

}
