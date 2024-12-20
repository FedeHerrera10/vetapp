package com.vet.app.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TimeValidation {
    public static boolean validate(String datetime ) {
        System.out.println(datetime);
        // Fecha y hora inicial en formato ISO 8601
        String dateTimeString = datetime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime initialDateTime = LocalDateTime.parse(dateTimeString, formatter);

        // Fecha y hora actual
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Calcular la duración entre las dos fechas
        Duration duration = Duration.between(initialDateTime, currentDateTime);

        // Validar si han pasado 10 minutos
        if (duration.toMinutes() >= 10) {
            return false;
        } 
        return true;
    }
}