package ru.practicum.collector.hubEventHandler;

import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class DeviceRemovedEventHandler
        extends AbstractKafkaHubEventHandler {

    public DeviceRemovedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    protected void setPayload(
            HubEventProto proto,
            HubEventAvro.Builder builder
    ) {
        DeviceRemovedEventProto payloadProto = proto.getDeviceRemoved();

        DeviceRemovedEventProto payloadAvro = DeviceRemovedEventProto.newBuilder()
                .setId(payloadProto.getId())
                .build();
        builder.setPayload(payloadAvro);
    }

}
