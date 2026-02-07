package ru.yandex.practicum.hubEventHandler;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Getter
public class HubEventHandlerRegistry {

    private final Map<String, HubEventHandler> handlers;

    public HubEventHandlerRegistry(List<HubEventHandler> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, h -> h));
    }
}
