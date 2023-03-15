package hr.fer.tel.rassus.radja.server.services;

import hr.fer.tel.rassus.radja.server.beans.Sensor;

import java.util.Map;

public interface SensorRepository {
    Integer register(Sensor sensor);

    Map<Integer, Sensor> allSensors();

    Sensor getSensorById(Integer id);

    boolean sensorRegistered(Integer sensorId);
}
