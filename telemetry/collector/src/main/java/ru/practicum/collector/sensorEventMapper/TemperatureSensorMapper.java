package ru.practicum.collector.sensorEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.sensorEventDto.SensorEventCommonDto;
import ru.practicum.collector.sensorEventDto.TemperatureSensorEventDto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorMapper
        implements SensorEventAvroMapper<TemperatureSensorEventDto> {

    @Override
    public boolean supports(SensorEventCommonDto event) {
        return event instanceof TemperatureSensorEventDto;
    }

    public SpecificRecordBase toAvro(TemperatureSensorEventDto event) {
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperature_c())
                .setTemperatureF(event.getTemperature_f())
                .build();
    }

    @Override
    public SpecificRecordBase toAvroGeneric(SensorEventCommonDto event) {
        return toAvro((TemperatureSensorEventDto) event);
    }
}
