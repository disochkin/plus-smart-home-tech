package ru.practicum.collector.hubEventDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScenarioConditionDto {
    private String sensorId;
    private ScenarioConditionType type;
    private ScenarioOperationType scenarioOperationType;
    private Integer value;
}
