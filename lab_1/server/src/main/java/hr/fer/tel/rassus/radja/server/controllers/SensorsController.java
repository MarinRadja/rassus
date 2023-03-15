package hr.fer.tel.rassus.radja.server.controllers;

import hr.fer.tel.rassus.radja.server.beans.Sensor;
import hr.fer.tel.rassus.radja.server.services.SensorRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/sensors")
public class SensorsController {
    @Autowired
    SensorRepository sensorRepo;

    //  TODO 4.1  Registracija
    @PostMapping(value="/register", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> register (@RequestBody Sensor sensor) {
        System.out.println("New sensor registration request.");
        Integer sensorId = sensorRepo.register(sensor);

        return new ResponseEntity<>("localhost:8090/sensors/" + sensorId, HttpStatus.CREATED);
    }

    //  TODO 4.4  Popis senzora
    @GetMapping(value="/all", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> listSensors() {
        JSONArray response = new JSONArray();

        for (Map.Entry<Integer, Sensor> entry : sensorRepo.allSensors().entrySet()) {
            Integer k = entry.getKey();
            Sensor s = entry.getValue();

            response.put(s.toJSON(k));
        }

        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    //  TODO 4.2  Najbliži susjed

    @GetMapping(value="/nearest/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> nearestNeighbour(@PathVariable Integer id) {
        Sensor sensorInfo = sensorRepo.getSensorById(id);

        if (sensorInfo == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Map.Entry<Integer, Sensor> currentNN = null;
        Double currentNNDistance = Double.MAX_VALUE;

        Map<Integer, Sensor> sensors = sensorRepo.allSensors();

        for (Map.Entry<Integer, Sensor> otherSensor : sensors.entrySet()) {
            Integer otherSensorId = otherSensor.getKey();
            if (otherSensorId == id)
                continue;

            Sensor otherSensorInfo = otherSensor.getValue();

            Double lat1 = sensorInfo.getLatitude();
            Double lon1 = sensorInfo.getLongitude();
            Double lat2 = otherSensorInfo.getLatitude();
            Double lon2 = otherSensorInfo.getLatitude();

            Double R = 6371.;
            Double dLon = lon2 - lon1;
            Double dLat = lat2 - lat1;
            Double a = Math.pow(Math.sin(dLat/2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon/2), 2);
            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            Double distance = R * c;

            if (distance < currentNNDistance) {
                distance = currentNNDistance;
                currentNN = otherSensor;
            }
        }

        if (currentNN == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        JSONObject nn = currentNN.getValue().toJSON(currentNN.getKey());
        return new ResponseEntity<>(nn.toString(), HttpStatus.OK);
    }

}
