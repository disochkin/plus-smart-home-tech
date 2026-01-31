package ru.practicum.collector.sensorEventHandler;

import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorSensorEventHandler
        extends AbstractKafkaSensorEventHandler {

    public TemperatureSensorSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    protected void setPayload(
            SensorEventProto proto,
            SensorEventAvro.Builder builder
    ) {
        TemperatureSensorProto payloadProto = proto.getTemperatureSensor();

        TemperatureSensorAvro payloadAvro = TemperatureSensorAvro.newBuilder()
                .setTemperatureC(payloadProto.getTemperatureC())
                .setTemperatureF(payloadProto.getTemperatureF())
                .build();

        builder.setPayload(payloadAvro);
    }

}
