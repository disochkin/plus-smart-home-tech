package ru.practicum.collector.sensorEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.collector.sensorEventDto.SensorEventCommonDto;

public interface SensorEventAvroMapper<T extends SensorEventCommonDto> {
    boolean supports(SensorEventCommonDto event);

    SpecificRecordBase toAvroGeneric(SensorEventCommonDto event);
}