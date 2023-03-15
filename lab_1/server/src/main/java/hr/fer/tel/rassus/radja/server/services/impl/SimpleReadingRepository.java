package hr.fer.tel.rassus.radja.server.services.impl;

import hr.fer.tel.rassus.radja.server.beans.Reading;
import hr.fer.tel.rassus.radja.server.services.ReadingRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class SimpleReadingRepository implements ReadingRepository {
    private Map<Integer, Reading> readingsRepository;
    private Integer freeId;

    public SimpleReadingRepository() {
        this.readingsRepository = new LinkedHashMap<>();
        freeId = 0;
    }

    @Override
    public void save(Reading reading) {
        Integer readingId = this.freeId;
        this.freeId += 1;

        this.readingsRepository.put(readingId, reading);
    }

    @Override
    public Map<Integer, Reading> allReadings() {
        return this.readingsRepository;
    }

}
