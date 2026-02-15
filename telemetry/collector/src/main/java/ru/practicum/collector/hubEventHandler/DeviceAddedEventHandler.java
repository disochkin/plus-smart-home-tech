package ru.practicum.collector.hubEventHandler;

import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class DeviceAddedEventHandler
        extends AbstractKafkaHubEventHandler {

    public DeviceAddedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    protected void setPayload(
            HubEventProto proto,
            HubEventAvro.Builder builder
    ) {
        DeviceAddedEventProto payloadProto = proto.getDeviceAdded();

        DeviceAddedEventAvro payloadAvro = DeviceAddedEventAvro.newBuilder()
                .setId(payloadProto.getId())
                .setType(DeviceTypeAvro.valueOf(payloadProto.getType().name()))
                .build();
        builder.setPayload(payloadAvro);
    }

}
