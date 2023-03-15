package hr.fer.tel.rassus.radja.server.services;

import hr.fer.tel.rassus.radja.server.beans.Reading;

import java.util.Map;

public interface ReadingRepository {

    void save(Reading reading);

    Map<Integer, Reading> allReadings();
}
