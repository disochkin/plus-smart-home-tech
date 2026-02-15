package ru.practicum.collector.hubEventHandler;

import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class ScenarioRemovedEventHandler
        extends AbstractKafkaHubEventHandler {

    public ScenarioRemovedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    protected void setPayload(
            HubEventProto proto,
            HubEventAvro.Builder builder
    ) {
        ScenarioRemovedEventProto payloadProto = proto.getScenarioRemoved();

        ScenarioRemovedEventProto payloadAvro = ScenarioRemovedEventProto.newBuilder()
                .setName(payloadProto.getName())
                .build();
        builder.setPayload(payloadAvro);
    }

}
