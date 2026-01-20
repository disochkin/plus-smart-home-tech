package ru.practicum.collector.hubEventDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
public class ScenarioAddedDto extends HubEventCommonDto {
    @NotBlank
    private String hubId;
    private Instant timestamp = Instant.now();
    @NotBlank
    private String id;

    @NotBlank
    @Size(min = 3)
    private String name;

    @NotNull
    private List<ScenarioConditionDto> conditions;

    @NotNull
    private List<DeviceAddedDto> actions;

    @NotNull
    HubEventType type;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
