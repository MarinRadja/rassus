package com.radja.temperaturemicroservice.controllers;

import com.radja.temperaturemicroservice.dto.TemperatureReadingDto;
import com.radja.temperaturemicroservice.services.impl.TemperatureReadingsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class ReadingController {
    private final TemperatureReadingsServiceImpl humidityReadingsService;

    @GetMapping("/temperature")
    public ResponseEntity<TemperatureReadingDto> getReading () {
        long seconds = System.currentTimeMillis() / 1000L;
        long row = seconds % 100;

        TemperatureReadingDto hrDto = humidityReadingsService.getByRow(row);

        return new ResponseEntity<>(hrDto, HttpStatus.FOUND);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<TemperatureReadingDto> getReadingById (@PathVariable long id) {
        TemperatureReadingDto hrDto = humidityReadingsService.getByRow(id);

        return new ResponseEntity<>(hrDto, HttpStatus.FOUND);
    }
}
