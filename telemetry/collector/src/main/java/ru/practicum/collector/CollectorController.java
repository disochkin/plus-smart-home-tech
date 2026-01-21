package ru.practicum.collector;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.collector.hubEventDto.HubEventCommonDto;
import ru.practicum.collector.hubEventMapper.HubEventMapperCommon;
import ru.practicum.collector.sensorEventDto.SensorEventCommonDto;
import ru.practicum.collector.sensorEventMapper.SensorEventMapperCommon;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CollectorController {
    private final KafkaClient client;
    private final SensorEventMapperCommon sensorEventMapperCommon;
    private final HubEventMapperCommon hubEventMapperCommon;

    @PostMapping("/events/sensors")
    public ResponseEntity<String> collectSensorEvent(@Valid @RequestBody SensorEventCommonDto event) {
        SpecificRecordBase avro = sensorEventMapperCommon.toAvro(event);
        log.debug("Запись в кафка: {}, ", avro.toString());
        ProducerRecord pr = new ProducerRecord<>("telemetry.sensors.v1", avro);
        client.getProducer().send(pr);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/events/hubs")
    public ResponseEntity<String> collectHubEvent(@Valid @RequestBody HubEventCommonDto event) {
        SpecificRecordBase avro = hubEventMapperCommon.toAvro(event);
        log.debug("Запись в кафка: {}, ", avro.toString());
        ProducerRecord pr = new ProducerRecord<>("telemetry.hubs.v1", avro);
        client.getProducer().send(pr);
        return ResponseEntity.ok("OK");
    }
}