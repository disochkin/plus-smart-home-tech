package ru.practicum.collector.hubEventHandler;

import org.springframework.stereotype.Component;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScenarioAddedEventHandler
        extends AbstractKafkaHubEventHandler {

    public ScenarioAddedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    protected void setPayload(
            HubEventProto proto,
            HubEventAvro.Builder builder
    ) {
        ScenarioAddedEventProto payloadProto = proto.getScenarioAdded();

        List<ScenarioConditionAvro> scenarioConditionAvros = payloadProto.getConditionList().stream()
                .map(ScenarioConditionSimpleMapper::toAvro)
                .collect(Collectors.toList());

        List<DeviceActionAvro> deviceActionAvros = payloadProto.getActionList().stream()
                .map(DeviceActionsSimpleMapper::toAvro)
                .collect(Collectors.toList());

        ScenarioAddedEventAvro payloadAvro = ScenarioAddedEventAvro.newBuilder()
                .setName(payloadProto.getName())
                .setConditions(scenarioConditionAvros)
                .setActions(deviceActionAvros)
                .build();
        builder.setPayload(payloadAvro);
    }

}
