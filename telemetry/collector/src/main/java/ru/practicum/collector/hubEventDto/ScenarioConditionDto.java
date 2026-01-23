package ru.practicum.collector.hubEventDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScenarioConditionDto {
    private String sensorId;
    private ConditionType type;
    private OperationType operation;
    private Integer value;
}
