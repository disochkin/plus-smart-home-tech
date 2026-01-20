package ru.practicum.collector.sensorEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.sensorEventDto.MotionSensorEventDto;
import ru.practicum.collector.sensorEventDto.SensorEventCommonDto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorMapper
        implements SensorEventAvroMapper<MotionSensorEventDto> {

    @Override
    public boolean supports(SensorEventCommonDto event) {
        return event instanceof MotionSensorEventDto;
    }

    public SpecificRecordBase toAvro(MotionSensorEventDto event) {
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setMotion(event.getMotion())
                .setVoltage(event.getVoltage())
                .build();
    }

    @Override
    public SpecificRecordBase toAvroGeneric(SensorEventCommonDto event) {
        return toAvro((MotionSensorEventDto) event);
    }
}
