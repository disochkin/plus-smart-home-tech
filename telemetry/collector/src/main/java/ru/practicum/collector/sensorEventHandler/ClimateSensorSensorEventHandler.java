package ru.practicum.collector.sensorEventHandler;

import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class ClimateSensorSensorEventHandler
        extends AbstractKafkaSensorEventHandler {

    public ClimateSensorSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    protected void setPayload(
            SensorEventProto proto,
            SensorEventAvro.Builder builder
    ) {
        ClimateSensorProto payloadProto = proto.getClimateSensor();

        ClimateSensorAvro payloadAvro = ClimateSensorAvro.newBuilder()
                .setTemperatureC(payloadProto.getTemperatureC())
                .setCo2Level(payloadProto.getCo2Level())
                .setHumidity(payloadProto.getHumidity())
                .build();

        builder.setPayload(payloadAvro);
    }

}
