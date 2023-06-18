package ru.practicum.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatMapper {

    public static StatSvc mapToStat(EndpointHit dto) {
        StatSvc newStat = new StatSvc();
        newStat.setApp(dto.getApp());
        newStat.setUri(dto.getUri());
        newStat.setIp(dto.getIp());
        LocalDateTime time = LocalDateTime.parse(dto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        newStat.setTimestamp(time);

        return newStat;
    }
}