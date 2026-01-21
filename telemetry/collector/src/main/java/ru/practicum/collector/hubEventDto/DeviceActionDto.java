package ru.practicum.collector.hubEventDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeviceActionDto {
    private String sensorId;
    private ActionType type;
    private Integer value;
}
