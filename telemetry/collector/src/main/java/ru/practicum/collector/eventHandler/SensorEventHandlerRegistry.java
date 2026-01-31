package ru.practicum.collector.eventHandler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SensorEventHandlerRegistry {

    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> handlers;

    public SensorEventHandlerRegistry(List<SensorEventHandler> handlerList) {
        this.handlers = handlerList.stream().collect(Collectors.toMap(
                SensorEventHandler::getMessageType,
                Function.identity()));
    }

    public void handle(SensorEventProto event) {
        SensorEventHandler handler = handlers.get(event.getPayloadCase());
        if (handler == null) {
            throw new IllegalStateException(
                    "Нет хендлера для " + event.getPayloadCase());
        }
        handler.handle(event);
    }
}
