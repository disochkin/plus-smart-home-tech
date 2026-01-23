package ru.practicum.collector.hubEventMapper;

import ru.practicum.collector.hubEventDto.ScenarioConditionDto;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

public class ScenarioConditionSimpleMapper {
    public static ScenarioConditionAvro toAvro(ScenarioConditionDto scenarioConditionDto) {
        ScenarioConditionAvro avro = new ScenarioConditionAvro();
        avro.setSensorId(scenarioConditionDto.getSensorId());
        avro.setType(ConditionTypeAvro.valueOf(scenarioConditionDto.getType().name()));
        avro.setOperation(ConditionOperationAvro.valueOf(scenarioConditionDto.getOperation().name()));
        avro.setValue(scenarioConditionDto.getValue());
        return avro;
    }
}
