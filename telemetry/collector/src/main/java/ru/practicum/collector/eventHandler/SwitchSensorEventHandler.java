package ru.practicum.collector.eventHandler;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import java.time.Instant;

@Component
public class SwitchSensorEventHandler
        extends AbstractKafkaEventHandler<SensorEventAvro> {
    public SwitchSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    protected SensorEventAvro payloadToAvro(SensorEventProto sensorEventProto) {
        SensorEventAvro sensorEventAvro = toAvroCommon(sensorEventProto);
        SwitchSensorProto payloadProto = sensorEventProto.getSwitchSensor();
        SwitchSensorAvro payloadAvro = SwitchSensorAvro.newBuilder()
                .setState(payloadProto.getState())
                .build();
        sensorEventAvro.setPayload(payloadAvro);
        return sensorEventAvro;
    }
}
