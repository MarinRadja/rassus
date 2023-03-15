package hr.fer.tel.rassus.radja.server.beans;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class Reading {
    private Integer sensorId;
    private Double temperature;
    private Double pressure;
    private Double humidity;
    private Double co;
    private Double no2;
    private Double so2;

    public Reading() {}

    public Reading(Integer sensorId, Double temperature, Double pressure, Double humidity, Double co, Double no2, Double so2) {
        this.sensorId = sensorId;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.co = co;
        this.no2 = no2;
        this.so2 = so2;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getPressure() {
        return pressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Double getCo() { return co; }

    public Double getNo2() {
        return no2;
    }

    public Double getSo2() {
        return so2;
    }

    public JSONObject toJSON() {
        JSONObject sensorToJSON = new JSONObject();
        sensorToJSON.put("sensorId", this.getSensorId());
        sensorToJSON.put("temperature", this.getTemperature());
        sensorToJSON.put("pressure", this.getPressure());
        sensorToJSON.put("humidity", this.getHumidity());
        sensorToJSON.put("co", this.getCo());
        sensorToJSON.put("no2", this.getNo2());
        sensorToJSON.put("so2", this.getSo2());

        return sensorToJSON;
    }

}

