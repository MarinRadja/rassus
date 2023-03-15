package hr.fer.tel.rassus.radja.sensor;


import com.opencsv.CSVReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Main {
    private final static Double minLat = 15.87;
    private final static  Double  maxLat = 16.;

    private final static Double minLong = 45.75;
    private final static Double maxLong = 45.85;

    private final static String ip = "127.0.0.1";
    private static Integer port;

    private static Integer sensorId;
    private static Double latitude;
    private static Double longitude;

    private  static String ipNN;
    private static Integer portNN;
    private static Double latitudeNN;
    private static Double longitudeNN;

    private static JSONObject nearestNeighbour;
    private static HttpClient httpClient;

    private static List<Map<String, Integer>> possibleReadings = new LinkedList<>();

    private static GRPCClient grpcClient;
    private static GRPCServer grpcServer;


    public static void main(String[] args) throws InterruptedException, IOException {
        Instant start = Instant.now();
        System.out.println("Started client.");

        port = Integer.parseInt(args[0]);
        // Reading currentReading;

        httpClient = HttpClients.createDefault();

        sensorId = register();

        if (sensorId == -1) {
            System.out.println("Failed to register. Stopping...");
            return;
        }

        System.out.println("Got id: " + sensorId);

        System.out.println("Loading readings csv.");
        loadCSV();
        System.out.println("Finished loading readings csv.");



        grpcServer = new GRPCServer(new CalibrateService(), port);
        grpcServer.start();
        updateServerInfo(possibleReadings.get(0));


        // wait for other clients to init
        Thread.sleep(10000);

        // get nn
        boolean foundNN = getNearestNeighbour();
        System.out.println("Found nearest neighbour? " + foundNN);

        grpcClient = new GRPCClient(ipNN, portNN);

        System.out.println("Sensor ID: " + sensorId + "\tGRPC port: " + port + "\tGRPC NN port: " + portNN);

        while (true) {
            // generate reading
            Instant finish = Instant.now();
            Long timeElapsed = Duration.between(start, finish).getSeconds();

            Map<String, Integer> newReading = possibleReadings.get(timeElapsed.intValue() % 100);
            // System.out.println(newReading.toString());
            updateServerInfo(newReading);


            // do the grpc magic
            Map<String, Double> calibratedReading = null;

            if (foundNN) {
                try {
                    Map<String, Integer> neighbourSensorData = grpcClient.requestSensorData();
                    calibratedReading = calibrateReading(newReading, neighbourSensorData);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    foundNN = false;
                }
            }
            else {
                calibratedReading = calibrateReading(newReading, newReading);
            }


            // send reading to server
            sendReading(calibratedReading);
            Thread.sleep(1600);
        }

    }

    private static Map<String, Double> calibrateReading(Map<String, Integer> newReading, Map<String, Integer> neighbourSensorData) {
        Map<String, Double> calibratedReading = new HashMap<>();

        Integer cTemp = newReading.get("temperature");
        Integer cPres = newReading.get("pressure");
        Integer cHum = newReading.get("humidity");
        Integer cCO = newReading.get("co");
        Integer cSO2 = newReading.get("so2");
        Integer cNO2 = newReading.get("no2");

        Integer nTemp = neighbourSensorData.get("temperature");
        Integer nPres = neighbourSensorData.get("pressure");
        Integer nHum = neighbourSensorData.get("humidity");
        Integer nCO = neighbourSensorData.get("co");
        Integer nSO2 = neighbourSensorData.get("so2");
        Integer nNO2 = neighbourSensorData.get("no2");

        if (cSO2 == 0) {
            cSO2 = nSO2;
        }
        else {
            cNO2 = nNO2;
        }

        if (nSO2 == 0) {
            nSO2 = cSO2;
        }
        else {
            nNO2 = cNO2;
        }

        calibratedReading.put("temperature", 1.*(cTemp + nTemp) / 2);
        calibratedReading.put("pressure", 1.*(cPres + nPres) / 2);
        calibratedReading.put("humidity", 1.*(cHum + nHum) / 2);
        calibratedReading.put("co", 1.*(cCO + nCO) / 2);
        calibratedReading.put("so2", 1.*(cSO2 + nSO2) / 2);
        calibratedReading.put("no2", 1.*(cNO2 + nNO2) / 2);

        return calibratedReading;
    }

    private static void updateServerInfo(Map<String, Integer> newReading) {
        System.out.println("Current reading: " + newReading.toString());
        grpcServer.getService().setCo(newReading.get("co"));
        grpcServer.getService().setTemperature(newReading.get("temperature"));
        grpcServer.getService().setHumidity(newReading.get("humidity"));
        grpcServer.getService().setPressure(newReading.get("pressure"));
        grpcServer.getService().setNo2(newReading.get("no2"));
        grpcServer.getService().setSo2(newReading.get("so2"));
    }

    private static void sendReading(Map<String, Double> calibratedReading) {

        JSONObject json = new JSONObject(calibratedReading);
        json.put("sensorId", sensorId);

        StringEntity readingDetails = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost("http://127.0.0.1:8090/readings/submit");
        post.setEntity(readingDetails);

        try {
            HttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 201) {
                String message = EntityUtils.toString(response.getEntity());
            }
        }
        catch (Exception ex){
            System.out.println("Something went wrong with contacting server. Shutting down..");
            System.exit(-1);
        }
    }

    private static boolean getNearestNeighbour() {
        HttpGet get = new HttpGet("http://127.0.0.1:8090/sensors/nearest/" + sensorId);

        try {
            HttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200 ) {
                nearestNeighbour = new JSONObject(EntityUtils.toString(response.getEntity()));

                ipNN = nearestNeighbour.get("ip").toString();
                portNN = Integer.parseInt(nearestNeighbour.get("port").toString());
                latitudeNN = Double.parseDouble(nearestNeighbour.get("latitude").toString());
                longitudeNN = Double.parseDouble(nearestNeighbour.get("longitude").toString());

                return true;
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private static Integer register() {
        Random random = new Random();
        latitude = minLat + (maxLat - minLat) * random.nextDouble();
        longitude = minLong + (maxLong - minLong) * random.nextDouble();



        Map<String, String> map = new LinkedHashMap<>();
        map.put("latitude", String.valueOf(latitude));
        map.put("longitude", String.valueOf(longitude));
        map.put("ip", ip);
        map.put("port", String.valueOf(port));

        JSONObject json = new JSONObject(map);


        StringEntity registrationDetails = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost("http://127.0.0.1:8090/sensors/register");
        post.setEntity(registrationDetails);

        try {
            HttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 201) {
                String url = EntityUtils.toString(response.getEntity());

                return Integer.parseInt(url.substring(url.lastIndexOf("/") + 1).trim());
            }
        }
        catch (Exception ex){
            return -1;
        }

        System.out.println("not supposed to reach here?");
        return -2;
    }

    private static void loadCSV() {
        try (CSVReader reader = new CSVReader(new FileReader("files/readings[4].csv"))) {
            List<String[]> r = reader.readAll();

            for (int i = 1; i < r.size(); i++) {
                Map<String, Integer> reading = new HashMap<>();

                reading.put("temperature", Integer.parseInt(r.get(i)[0]));
                reading.put("pressure", Integer.parseInt(r.get(i)[1]));
                reading.put("humidity", Integer.parseInt(r.get(i)[2]));
                reading.put("co", Integer.parseInt(r.get(i)[3]));

                Integer no2;
                if (r.get(i)[4].length() == 0) {
                    reading.put("no2", 0);
                    reading.put("so2", Integer.parseInt(r.get(i)[5]));
                }
                else {
                    reading.put("no2", Integer.parseInt(r.get(i)[4]));
                    reading.put("so2", 0);
                }

                possibleReadings.add(reading);
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
}
