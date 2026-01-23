package ru.practicum.collector.sensorEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.sensorEventDto.LightSensorEventDto;
import ru.practicum.collector.sensorEventDto.SensorEventCommonDto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorMapper
        implements SensorEventAvroMapper<LightSensorEventDto> {

    @Override
    public boolean supports(SensorEventCommonDto event) {
        return event instanceof LightSensorEventDto;
    }

    public SpecificRecordBase toAvro(LightSensorEventDto event) {
        return LightSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setLuminosity(event.getLuminosity())
                .build();
    }

    @Override
    public SpecificRecordBase toAvroGeneric(SensorEventCommonDto event) {
        return toAvro((LightSensorEventDto) event);
    }
}
