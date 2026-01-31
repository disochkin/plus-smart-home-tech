package ru.practicum.collector.hubEventHandler;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

public class DeviceActionsSimpleMapper {
    public static DeviceActionAvro toAvro(DeviceActionProto deviceActionProto) {
        DeviceActionAvro avro = new DeviceActionAvro();
        avro.setType(ActionTypeAvro.valueOf(deviceActionProto.getType().name()));
        avro.setValue(deviceActionProto.getValue());
        avro.setSensorId(deviceActionProto.getSensorId());
        return avro;
    }
}