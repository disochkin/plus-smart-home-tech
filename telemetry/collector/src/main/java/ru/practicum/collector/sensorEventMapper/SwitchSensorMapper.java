package ru.practicum.collector.sensorEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.sensorEventDto.SensorEventCommonDto;
import ru.practicum.collector.sensorEventDto.SwitchSensorEventDto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorMapper
        implements SensorEventAvroMapper<SensorEventCommonDto> {

    @Override
    public boolean supports(SensorEventCommonDto event) {
        return event instanceof SwitchSensorEventDto;
    }

    public SpecificRecordBase toAvro(SwitchSensorEventDto event) {
        return SwitchSensorAvro.newBuilder()
                .setState(event.getState())
                .build();
    }

    @Override
    public SpecificRecordBase toAvroGeneric(SensorEventCommonDto event) {
        return toAvro((SwitchSensorEventDto) event);
    }
}
