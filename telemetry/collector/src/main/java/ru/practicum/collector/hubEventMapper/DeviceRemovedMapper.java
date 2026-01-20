package ru.practicum.collector.hubEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.hubEventDto.DeviceAddedDto;
import ru.practicum.collector.hubEventDto.DeviceRemovedDto;
import ru.practicum.collector.hubEventDto.HubEventCommonDto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceType;

@Component
public class DeviceRemovedMapper implements HubEventAvroMapper<DeviceRemovedDto> {

    @Override
    public boolean supports(HubEventCommonDto event) {
        return event instanceof DeviceRemovedDto;
    }

    public SpecificRecordBase toAvro(DeviceRemovedDto event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    @Override
    public SpecificRecordBase toAvroGeneric(HubEventCommonDto event) {
        return toAvro((DeviceRemovedDto) event);
    }
}
