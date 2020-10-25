package com.kyberpunk.iot.watering.dto;

import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DateFormatHelper {
    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return "";
        return localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(LocaleContextHolder.getLocale()));
    }
}
