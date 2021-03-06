package com.iotinall.framework.constants.constinterface;

import java.time.format.DateTimeFormatter;

public interface DateTimeFormatters {
    DateTimeFormatter STANDARD_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter STANDARD_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter STANDARD_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
