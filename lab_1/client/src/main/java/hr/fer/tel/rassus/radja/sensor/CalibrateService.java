package hr.fer.tel.rassus.radja.sensor;

import hr.fer.tel.rassus.examples.CalibrateGrpc;
import hr.fer.tel.rassus.examples.Request;
import hr.fer.tel.rassus.examples.Response;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class CalibrateService extends CalibrateGrpc.CalibrateImplBase {
    private static final Logger logger = Logger.getLogger(CalibrateService.class.getName());

    private Integer temperature;
    private Integer pressure;
    private Integer humidity;
    private Integer co;
    private Integer no2;
    private Integer so2;


    public void requestSensorData(Request request, StreamObserver<Response> responseObserver) {
        logger.info("Need to send response to port: " + request.getPort());

        // Create response
        Response response = Response.newBuilder()
                .setTemperature(this.temperature)
                .setPressure(this.pressure)
                .setHumidity(this.humidity)
                .setCo(this.co)
                .setNo2(this.no2)
                .setSo2(this.so2)
                .build();

        // Send response
        responseObserver.onNext(
                response
        );
        logger.info("Responding with: " + this.toString());

        // Send a notification of successful stream completion.
        responseObserver.onCompleted();
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public void setCo(Integer co) {
        this.co = co;
    }

    public void setNo2(Integer no2) {
        this.no2 = no2;
    }

    public void setSo2(Integer so2) {
        this.so2 = so2;
    }

    @Override
    public String toString() {
        return "{" +
                "temperature=" + temperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", co=" + co +
                ", no2=" + no2 +
                ", so2=" + so2 +
                '}';
    }
}
