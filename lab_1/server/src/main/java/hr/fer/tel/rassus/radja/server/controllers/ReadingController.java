package hr.fer.tel.rassus.radja.server.controllers;

import hr.fer.tel.rassus.radja.server.beans.Reading;
import hr.fer.tel.rassus.radja.server.services.ReadingRepository;
import hr.fer.tel.rassus.radja.server.services.SensorRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/readings")
public class ReadingController {
    @Autowired
    ReadingRepository readingRepo;
    @Autowired
    SensorRepository sensorRepo;

    // TODO 4.3  Spremanje očitanja pojedinog senzora
    @PostMapping(value="/submit", produces= MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> register (@RequestBody Reading reading) {
      System.out.println("New reading submission by sensor " + reading.getSensorId() + ".");
      if (sensorRepo.sensorRegistered(reading.getSensorId())) {
          System.out.println("Reading is: " + reading.toJSON().toString());
          readingRepo.save(reading);

          return new ResponseEntity<>("Reading saved.", HttpStatus.CREATED);
      }
      return new ResponseEntity<>("This sensor's id is not registered.", HttpStatus.NO_CONTENT);
    }

    // TODO 4.5  Popis očitanja pojedinog senzora
    @GetMapping(value="/sensor/{sensorId}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> listSensors(@PathVariable Integer sensorId) {
        JSONArray response = new JSONArray();

        for (Map.Entry<Integer, Reading> reading : readingRepo.allReadings().entrySet()) {
            Integer k = reading.getKey();
            Reading r = reading.getValue();

            System.out.println(r);
            System.out.println(r.toJSON().toString());

            if (r.getSensorId().equals(sensorId))
                response.put(r.toJSON());
        }

        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}