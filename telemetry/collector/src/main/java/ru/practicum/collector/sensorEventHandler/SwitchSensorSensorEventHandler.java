package ru.practicum.collector.sensorEventHandler;

import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorSensorEventHandler
        extends AbstractKafkaSensorEventHandler {

    public SwitchSensorSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    protected void setPayload(
            SensorEventProto proto,
            SensorEventAvro.Builder builder
    ) {
        SwitchSensorProto payloadProto = proto.getSwitchSensor();

        SwitchSensorAvro payloadAvro = SwitchSensorAvro.newBuilder()
                .setState(payloadProto.getState())
                .build();

        builder.setPayload(payloadAvro);
    }

}
