package ru.yandex.practicum.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.hubEventHandler.HubEventHandler;
import ru.yandex.practicum.hubEventHandler.HubEventHandlerRegistry;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {
    private final KafkaConsumer<String, HubEventAvro> hubEventConsumer;
    private final HubEventHandlerRegistry hubEventHandlerRegistry;
    @Value("${kafka.topics.hubs}")
    private String hubsTopic;

    @Override
    public void run() {
        try {
            hubEventConsumer.subscribe(List.of(hubsTopic));
            log.info("HubEventProcessor. Подписка на топик событий хабов: {}", List.of(hubsTopic));
            Runtime.getRuntime().addShutdownHook(new Thread(hubEventConsumer::wakeup));
            Map<String, HubEventHandler> handlerMap = hubEventHandlerRegistry.getHandlers();
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = hubEventConsumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventAvro event = record.value();
                    String payloadName = event.getPayload().getClass().getSimpleName();
                    log.info("Получено событие: {}", payloadName);
                    if (handlerMap.containsKey(payloadName)) {
                        handlerMap.get(payloadName).handle(event);
                    } else {
                        throw new IllegalArgumentException("Не найден обработчки событий для: " + event);
                    }
                }
                hubEventConsumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка чтения данных из топика {}", hubsTopic);
        } finally {
            try {
                hubEventConsumer.commitSync();
            } finally {
                hubEventConsumer.close();
            }
        }
    }
}