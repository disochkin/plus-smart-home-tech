package ru.practicum.collector.hubEventMapper;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import ru.practicum.collector.hubEventDto.HubEventCommonDto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HubEventMapperCommon {

    private final List<HubEventAvroMapper<?>> mappers;

    public SpecificRecordBase toAvro(HubEventCommonDto event) {
        SpecificRecordBase payload = mappers.stream()
                .filter(m -> m.supports(event))
                .findFirst()
                .map(m -> m.toAvroGeneric(event))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No mapper for " + event.getClass()
                        ));
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}