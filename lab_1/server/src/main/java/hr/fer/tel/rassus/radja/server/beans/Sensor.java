package hr.fer.tel.rassus.radja.server.beans;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class Sensor {
    private Double latitude;
    private Double longitude;
    private String ip;
    private Integer port;

    public Sensor() {}

    public Sensor(Double latitude, Double longitude, String ipAddress, Integer port) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.ip = ipAddress;
        this.port = port;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public JSONObject toJSON(Integer id) {
        JSONObject sensorToJSON = new JSONObject();
        sensorToJSON.put("id", id);
        sensorToJSON.put("latitude", this.getLatitude());
        sensorToJSON.put("longitude", this.getLongitude());
        sensorToJSON.put("ip", this.getIp());
        sensorToJSON.put("port", this.getPort());

        return sensorToJSON;
    }
}
