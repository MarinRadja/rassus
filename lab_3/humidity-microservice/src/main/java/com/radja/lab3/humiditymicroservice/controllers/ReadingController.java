package com.radja.lab3.humiditymicroservice.controllers;

import com.radja.lab3.humiditymicroservice.dto.HumidityReadingDto;
import com.radja.lab3.humiditymicroservice.model.HumidityReading;
import com.radja.lab3.humiditymicroservice.services.impl.HumidityReadingsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class ReadingController {
    private final HumidityReadingsServiceImpl humidityReadingsService;

    @GetMapping("/humidity")
    public ResponseEntity<HumidityReadingDto> getReading () {
        long seconds = System.currentTimeMillis() / 1000L;
        long row = seconds % 100;

        HumidityReadingDto hrDto = humidityReadingsService.getByRow(row);

        return new ResponseEntity<>(hrDto, HttpStatus.FOUND);
    }
    @GetMapping("/by-id/{id}")
    public ResponseEntity<HumidityReadingDto> getReadingById (@PathVariable long id) {
        HumidityReadingDto hrDto = humidityReadingsService.getByRow(id);

        return new ResponseEntity<>(hrDto, HttpStatus.FOUND);
    }

    @GetMapping("/test")
    public String test(){
        return "test successful";
    }
}
