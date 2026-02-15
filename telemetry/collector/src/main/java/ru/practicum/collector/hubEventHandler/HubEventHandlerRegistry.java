package ru.practicum.collector.hubEventHandler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class HubEventHandlerRegistry {

    private final Map<HubEventProto.PayloadCase, HubEventHandler> handlers;

    public HubEventHandlerRegistry(List<HubEventHandler> handlerList) {
        this.handlers = handlerList.stream().collect(Collectors.toMap(
                HubEventHandler::getMessageType,
                Function.identity()));
    }

    public void handle(HubEventProto event) {
        HubEventHandler handler = handlers.get(event.getPayloadCase());
        if (handler == null) {
            throw new IllegalStateException(
                    "Нет хендлера для " + event.getPayloadCase());
        }
        handler.handle(event);
    }
}
