package ru.practicum.collector.hubEventDto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.collector.sensorEventDto.*;

import java.time.Instant;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = HubEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedDto.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedDto.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedDto.class, name = "SCENARIO_ADDED"),
      //  @JsonSubTypes.Type(value = MotionSensorEventDto.class, name = "MOTION_SENSOR_EVENT"),
      //  @JsonSubTypes.Type(value = TemperatureSensorEventDto.class, name = "TEMPERATURE_SENSOR_EVENT")
})

@Data
public abstract class HubEventCommonDto {
    @NotBlank
    private String hubId;
    private Instant timestamp = Instant.now();

    public abstract HubEventType getType();
}