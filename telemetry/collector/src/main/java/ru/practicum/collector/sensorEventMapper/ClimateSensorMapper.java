package ru.practicum.collector.sensorEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.sensorEventDto.ClimateSensorEventDto;
import ru.practicum.collector.sensorEventDto.SensorEventCommonDto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorMapper
        implements SensorEventAvroMapper<ClimateSensorEventDto> {

    @Override
    public boolean supports(SensorEventCommonDto event) {
        return event instanceof ClimateSensorEventDto;
    }

    public SpecificRecordBase toAvro(ClimateSensorEventDto event) {
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(event.getCo2Level())
                .setHumidity(event.getHumidity())
                .setTemperatureC(event.getTemperatureC())
                .build();
    }

    @Override
    public SpecificRecordBase toAvroGeneric(SensorEventCommonDto event) {
        return toAvro((ClimateSensorEventDto) event);
    }
}
