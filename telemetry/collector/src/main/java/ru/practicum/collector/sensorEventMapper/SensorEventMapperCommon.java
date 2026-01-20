package ru.practicum.collector.sensorEventMapper;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import ru.practicum.collector.sensorEventDto.SensorEventCommonDto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorEventMapperCommon {

    private final List<SensorEventAvroMapper<?>> mappers;

    public SpecificRecordBase toAvro(SensorEventCommonDto event) {
        SpecificRecordBase payload =  mappers.stream()
                .filter(m -> m.supports(event))
                .findFirst()
                .map(m -> m.toAvroGeneric(event))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No mapper for " + event.getClass()
                        ));
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}