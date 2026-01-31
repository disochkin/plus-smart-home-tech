package ru.practicum.collector.hubEventHandler;

import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

public class ScenarioConditionSimpleMapper {
    public static ScenarioConditionAvro toAvro(ScenarioConditionProto scenarioConditionProto) {
        ScenarioConditionAvro avro = new ScenarioConditionAvro();
        avro.setSensorId(scenarioConditionProto.getSensorId());
        avro.setType(ConditionTypeAvro.valueOf(scenarioConditionProto.getType().name()));
        avro.setOperation(ConditionOperationAvro.valueOf(scenarioConditionProto.getOperation().name()));
        switch (scenarioConditionProto.getValueCase()) {
            case INT_VALUE:
                avro.setValue(scenarioConditionProto.getIntValue());
                break;

            case BOOL_VALUE:
                avro.setValue(scenarioConditionProto.getBoolValue());
                break;

            case VALUE_NOT_SET:
                avro.setValue(null);
                break;

            default:
                throw new IllegalStateException(
                        "Unsupported value type: " + scenarioConditionProto.getValueCase()
                );
        }
        return avro;
    }
}