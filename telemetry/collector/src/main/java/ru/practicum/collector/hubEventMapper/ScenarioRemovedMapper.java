package ru.practicum.collector.hubEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.hubEventDto.HubEventCommonDto;
import ru.practicum.collector.hubEventDto.ScenarioRemovedDto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedMapper implements HubEventAvroMapper<ScenarioRemovedDto> {

    @Override
    public boolean supports(HubEventCommonDto event) {
        return event instanceof ScenarioRemovedDto;
    }

    public SpecificRecordBase toAvro(ScenarioRemovedDto scenarioRemovedDto) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedDto.getName())
                .build();
    }

    @Override
    public SpecificRecordBase toAvroGeneric(HubEventCommonDto event) {
        return toAvro((ScenarioRemovedDto) event);
    }
}
