package com.radja.aggregatormicroservice.controllers;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.radja.aggregatormicroservice.dto.AggregatedResultDto;
import com.radja.aggregatormicroservice.dto.HumidityReadingDto;
import com.radja.aggregatormicroservice.dto.TemperatureReadingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("")
public class AggregatorController {
    @Value("${temperature-unit}")
    private String temperatureUnit;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/get-readings")
    public ResponseEntity<AggregatedResultDto> getReadings () {
        String urlHumidity = "http://humidity-microservice/humidity";
        HumidityReadingDto hrDto = restTemplate.getForObject(urlHumidity, HumidityReadingDto.class);

        String urlTemperature = "http://temperature-microservice/temperature";
        TemperatureReadingDto tDto = restTemplate.getForObject(urlTemperature, TemperatureReadingDto.class);

        if (temperatureUnit.equals("K")) {
            tDto.setValue(tDto.getValue() + 273);
            tDto.setUnit("K");
        }

        AggregatedResultDto arDto = new AggregatedResultDto(hrDto, tDto);

        return new ResponseEntity<>(arDto, HttpStatus.OK);
    }
}
