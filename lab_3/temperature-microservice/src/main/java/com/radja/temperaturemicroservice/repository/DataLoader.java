package com.radja.temperaturemicroservice.repository;

import com.opencsv.CSVReader;
import com.radja.temperaturemicroservice.services.impl.TemperatureReadingsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {
    private TemperatureReadingsServiceImpl temperatureReadingsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream readings = new ClassPathResource("readings[4].csv").getInputStream();

        try (CSVReader reader = new CSVReader(new InputStreamReader(readings))) {
            List<String[]> r = reader.readAll();
            for (int i = 1; i <= 100; i++) {
                Integer temperature = Integer.parseInt(r.get(i)[0]);

                temperatureReadingsService.saveReading("C", temperature);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (final NumberFormatException e) {
            System.out.println("Cannot parse integer.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }
}
