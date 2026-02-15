package ru.practicum.collector.sensorEventHandler;

import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class MotionSensorSensorEventHandler
        extends AbstractKafkaSensorEventHandler {

    public MotionSensorSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    protected void setPayload(
            SensorEventProto proto,
            SensorEventAvro.Builder builder
    ) {
        MotionSensorProto payloadProto = proto.getMotionSensor();

        MotionSensorAvro payloadAvro = MotionSensorAvro.newBuilder()
                .setLinkQuality(payloadProto.getLinkQuality())
                .setMotion(payloadProto.getMotion())
                .setVoltage(payloadProto.getVoltage())
                .build();

        builder.setPayload(payloadAvro);
    }

}
