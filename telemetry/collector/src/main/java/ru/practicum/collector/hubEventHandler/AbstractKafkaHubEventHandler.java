package ru.practicum.collector.hubEventHandler;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractKafkaHubEventHandler<T extends SpecificRecordBase>
        implements HubEventHandler {

    protected static final String TOPIC = "telemetry.hubs.v1";
    protected final KafkaClient kafkaClient;

    protected static HubEventAvro.Builder baseBuilder(
            HubEventProto hubEventProto
    ) {
        Timestamp ts = hubEventProto.getTimestamp();
        return HubEventAvro.newBuilder()
                .setHubId(hubEventProto.getHubId())
                .setTimestamp(
                        Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos())
                );
    }

    protected abstract void setPayload(
            HubEventProto proto,
            HubEventAvro.Builder builder
    );

    public final void handle(HubEventProto event) {
        HubEventAvro.Builder builder = baseBuilder(event);
        setPayload(event, builder);
        HubEventAvro avro = builder.build();
        sendToKafka(avro);
    }

    protected void sendToKafka(SpecificRecordBase event) {
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(TOPIC, event);

        kafkaClient.getProducer()
                .send(record, (metadata, exception) -> {
                    if (exception != null) {
                        log.error(
                                "Ошибка Kafka [{}]: {}",
                                getMessageType(),
                                exception.getMessage(),
                                exception
                        );
                    } else {
                        log.info(
                                "Kafka OK [{}]: topic={}, partition={}, offset={}",
                                getMessageType(),
                                metadata.topic(),
                                metadata.partition(),
                                metadata.offset()
                        );
                    }
                });
    }

}
