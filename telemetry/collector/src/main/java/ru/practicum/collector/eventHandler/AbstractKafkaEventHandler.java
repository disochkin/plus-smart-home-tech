package ru.practicum.collector.eventHandler;

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
public abstract class AbstractKafkaEventHandler<T extends SpecificRecordBase>
implements SensorEventHandler {

    protected final KafkaClient kafkaClient;

    protected static final String TOPIC = "telemetry.sensors.v1";

    protected abstract SensorEventAvro payloadToAvro(SensorEventProto event);

    @Override
    public final void handle(SensorEventProto event)  {
        SensorEventAvro avroEvent = payloadToAvro(event);
        sendToKafka(avroEvent);
    }

    protected static SensorEventAvro toAvroCommon(SensorEventProto sensorEventProto) {
        Timestamp ts = sensorEventProto.getTimestamp();
        return SensorEventAvro.newBuilder()
                .setId(sensorEventProto.getId())
                .setHubId(sensorEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos()))
                .build();
    }

    protected void sendToKafka(SpecificRecordBase event) {
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(
                        TOPIC,
                        event
                );

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
                } );
    };
    }
    /* ===== Extension points ===== */

//    protected String getKafkaKey(SensorEventProto event) {
//        return null; // по умолчанию без ключа
//    }
//
//    protected void validate(SensorEventProto event) {
//        // по умолчанию ничего
//    }
//
//    protected void beforeSend(SensorEventProto event) {
//        // hook
//    }
//
//    protected void afterSend(SensorEventProto event) {
//        // hook
//    }

//    protected void handleError(SpecificRecordBase event, Exception e) throws Exception {
//        log.error(
//                "Ошибка обработки события [{}]",
//                getMessageType(),
//                e
//        );
//        throw e;
//    }
//}
