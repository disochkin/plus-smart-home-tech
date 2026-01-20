package ru.practicum.collector.sensorEventDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TemperatureSensorEventDto extends SensorEventCommonDto {
    private Integer temperature_c;
    private Integer temperature_f;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
