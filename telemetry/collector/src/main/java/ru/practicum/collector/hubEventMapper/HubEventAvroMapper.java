package ru.practicum.collector.hubEventMapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.collector.hubEventDto.HubEventCommonDto;

public interface HubEventAvroMapper<T extends HubEventCommonDto> {
    boolean supports(HubEventCommonDto event);

    SpecificRecordBase toAvroGeneric(HubEventCommonDto event);
}