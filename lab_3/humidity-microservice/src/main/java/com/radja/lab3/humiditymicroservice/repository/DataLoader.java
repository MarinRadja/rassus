package com.radja.lab3.humiditymicroservice.repository;

import com.opencsv.CSVReader;
import com.radja.lab3.humiditymicroservice.services.impl.HumidityReadingsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {
    private HumidityReadingsServiceImpl humidityReadingsService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream readings = new ClassPathResource("readings[4].csv").getInputStream();

        try (CSVReader reader = new CSVReader(new InputStreamReader(readings))) {
            List<String[]> r = reader.readAll();
            for (int i = 1; i <= 100; i++) {
                Integer humidity = Integer.parseInt(r.get(i)[2]);

                humidityReadingsService.saveReading("%", humidity);
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
