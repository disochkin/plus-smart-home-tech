package ru.practicum.collector.hubEventMapper;

import ru.practicum.collector.hubEventDto.DeviceActionDto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

public class DeviceActionsSimpleMapper {
    public static DeviceActionAvro toAvro(DeviceActionDto deviceActionDto) {
        DeviceActionAvro avro = new DeviceActionAvro();
        avro.setType(ActionTypeAvro.valueOf(deviceActionDto.getType().name()));
        avro.setValue(deviceActionDto.getValue());
        avro.setSensorId(deviceActionDto.getSensorId());
        return avro;
    }
}
