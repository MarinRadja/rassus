package hr.fer.tel.rassus.radja.sensor;

import hr.fer.tel.rassus.examples.CalibrateGrpc;
import hr.fer.tel.rassus.examples.Request;
import hr.fer.tel.rassus.examples.Response;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The type Simple unary rpc client.
 */
public class GRPCClient {

  private static final Logger logger = Logger.getLogger(GRPCClient.class.getName());


  private final ManagedChannel channel;
  private final CalibrateGrpc.CalibrateBlockingStub calibrateBlockingStub;



  private Integer port;

  /**
   * Instantiates a new Simple unary rpc client.
   *
   * @param host the host
   * @param port the port
   */
  public GRPCClient(String host, int port) {
    this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    this.port = port;

    calibrateBlockingStub = CalibrateGrpc.newBlockingStub(channel);
  }

  /**
   * Stop the client.
   *
   * @throws InterruptedException the interrupted exception
   */
  public void stop() throws InterruptedException {
//    Initiates an orderly shutdown in which preexisting calls continue but new calls are
//    immediately cancelled. Waits for the channel to become terminated, giving up if the timeout
//    is reached.
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  /**
   * Request uppercase.
   */
  public Map<String, Integer> requestSensorData() {

    Request request = Request.newBuilder()
        .setPort(this.port)
        .build();

    logger.info("Sending request for sensor data to port: " + this.port);
    try {
      Response response = calibrateBlockingStub.requestSensorData(request);

      Map<String, Integer> sensorData = new HashMap<>();
      sensorData.put("temperature", response.getTemperature());
      sensorData.put("pressure", response.getPressure());
      sensorData.put("humidity", response.getHumidity());
      sensorData.put("co", response.getCo());
      sensorData.put("no2", response.getNo2());
      sensorData.put("so2", response.getSo2());
      logger.info("Received nearest sensor's data: " + sensorData.toString());

      return sensorData;
    } catch (StatusRuntimeException e) {
      logger.info(e.getCause().toString());
      logger.info("RPC failed: " + e.getMessage());
    }
    return null;
  }

}
