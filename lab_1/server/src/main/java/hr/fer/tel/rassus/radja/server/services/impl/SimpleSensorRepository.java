package hr.fer.tel.rassus.radja.server.services.impl;

import hr.fer.tel.rassus.radja.server.beans.Sensor;
import hr.fer.tel.rassus.radja.server.services.SensorRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;


@Repository
public class SimpleSensorRepository implements SensorRepository {
    private Map<Integer, Sensor> sensorsRepository;
    private Integer freeId;

    public SimpleSensorRepository() {
        this.sensorsRepository = new LinkedHashMap<>();
        freeId = 0;
    }

    @Override
    public Integer register(Sensor sensor) {
        Integer sensorId = this.freeId;
        this.freeId += 1;

        this.sensorsRepository.put(sensorId, sensor);
        return sensorId;
    }

    @Override
    public Map<Integer, Sensor> allSensors(){
        return this.sensorsRepository;
    }

    @Override
    public Sensor getSensorById(Integer id) {
        return this.sensorsRepository.get(id);
    }

    @Override
    public boolean sensorRegistered(Integer sensorId) {
        return sensorsRepository.containsKey(sensorId);
    }
}
