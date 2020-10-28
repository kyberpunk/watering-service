package com.kyberpunk.iot.watering.dto;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DateFormatHelper {
    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return "";
        return localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(LocaleContextHolder.getLocale()));
    }

    public static String pretty(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return "";
        PrettyTime pretty = new PrettyTime(LocaleContextHolder.getLocale());
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return pretty.format(date);
    }
}
