package ru.practicum.collector.sensorEventHandler;

import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class LightSensorSensorEventHandler
        extends AbstractKafkaSensorEventHandler {

    public LightSensorSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }

    @Override
    protected void setPayload(
            SensorEventProto proto,
            SensorEventAvro.Builder builder
    ) {
        LightSensorProto payloadProto = proto.getLightSensor();

        LightSensorAvro payloadAvro = LightSensorAvro.newBuilder()
                .setLinkQuality(payloadProto.getLinkQuality())
                .setLuminosity(payloadProto.getLuminosity())
                .build();

        builder.setPayload(payloadAvro);
    }

}
