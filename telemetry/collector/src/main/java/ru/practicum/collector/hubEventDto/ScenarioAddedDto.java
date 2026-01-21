package ru.practicum.collector.hubEventDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ScenarioAddedDto extends HubEventCommonDto {

    @NotBlank
    @Size(min = 3)
    private String name;

    @NotNull
    private List<ScenarioConditionDto> conditions;

    @NotNull
    private List<DeviceActionDto> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
