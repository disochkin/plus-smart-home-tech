package ru.practicum.collector.sensorEventHandler;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.practicum.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractKafkaSensorEventHandler<T extends SpecificRecordBase>
        implements SensorEventHandler {

    protected static final String TOPIC = "telemetry.sensors.v1";
    protected final KafkaClient kafkaClient;

    protected static SensorEventAvro.Builder baseBuilder(
            SensorEventProto sensorEventProto
    ) {
        Timestamp ts = sensorEventProto.getTimestamp();

        return SensorEventAvro.newBuilder()
                .setId(sensorEventProto.getId())
                .setHubId(sensorEventProto.getHubId())
                .setTimestamp(
                        Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos())
                );
    }

    protected abstract void setPayload(
            SensorEventProto proto,
            SensorEventAvro.Builder builder
    );

    @Override
    public final void handle(SensorEventProto event) {
        SensorEventAvro.Builder builder = baseBuilder(event);
        setPayload(event, builder);
        SensorEventAvro avro = builder.build();
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
