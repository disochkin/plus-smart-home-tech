package ru.practicum.collector.hubEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.collector.hubEventDto.HubEventCommonDto;
import ru.practicum.collector.hubEventDto.ScenarioAddedDto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@Component
public class ScenarioAddedMapper implements HubEventAvroMapper<ScenarioAddedDto> {

    @Override
    public boolean supports(HubEventCommonDto event) {
        return event instanceof ScenarioAddedDto;
    }

    public SpecificRecordBase toAvro(ScenarioAddedDto scenarioAddedDto) {
        List<ScenarioConditionAvro> scenarioConditionAvros = scenarioAddedDto.getConditions().stream()
                .map(ScenarioConditionSimpleMapper::toAvro)
                .toList();

        List<DeviceActionAvro> deviceActionAvros = scenarioAddedDto.getActions().stream()
                .map(DeviceActionsSimpleMapper::toAvro)
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedDto.getName())
                .setConditions(scenarioConditionAvros)
                .setActions(deviceActionAvros)
                .build();
    }

    @Override
    public SpecificRecordBase toAvroGeneric(HubEventCommonDto event) {
        return toAvro((ScenarioAddedDto) event);
    }
}
