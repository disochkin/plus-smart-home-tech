package ru.practicum.collector.hubEventDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class ScenarioRemovedEventDto extends HubEventCommonDto {
    @NotNull
    HubEventType type;
    @NotBlank
    private String hubId;
    private final Instant timestamp = Instant.now();
    @NotBlank
    @Size(min = 3)
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
