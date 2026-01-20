package ru.practicum.collector.hubEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.hubEventDto.DeviceAddedDto;
import ru.practicum.collector.hubEventDto.HubEventCommonDto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceType;

@Component
public class ScenarioAddedMapper implements HubEventAvroMapper<DeviceAddedDto> {

    @Override
    public boolean supports(HubEventCommonDto event) {
        return event instanceof DeviceAddedDto;
    }

    public SpecificRecordBase toAvro(DeviceAddedDto event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setDeviceType(DeviceType.valueOf(event.getDeviceType().name()))
                .build();
    }

    @Override
    public SpecificRecordBase toAvroGeneric(HubEventCommonDto event) {
        return toAvro((DeviceAddedDto) event);
    }
}
