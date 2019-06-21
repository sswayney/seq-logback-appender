package com.conversantmedia.cpeui.shared.util.seq;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.text.Format;
import java.util.Date;

/**
 * Model decorated to help with the serialization of a seq log event
 * Contains the base seq params and basic logback params
 */
@Getter
@Setter
public class SeqLogEntry implements java.io.Serializable {

    @JsonProperty("@t")
    private String time;

    @JsonProperty("@m")
    private String message;

    @JsonProperty("@mt")
    private String messageTemplate;

    @JsonProperty("@x")
    private String exception;

    @JsonProperty("@l")
    private String level;

    @JsonProperty("@i")
    private String eventId;

    @JsonProperty("@r")
    private String renderings;

    @JsonProperty("ThreadName")
    private String threadName;

    /**
     * Constructor
     * @param eventObject The logback event
     * @param dateFormat Date format for timestamp
     */
    public SeqLogEntry(ILoggingEvent eventObject, Format dateFormat) {
        Date date = new Date(eventObject.getTimeStamp());
        this.setTime(dateFormat.format(date));
        this.setMessage(eventObject.getFormattedMessage());
        this.setLevel(eventObject.getLevel().levelStr);
        this.setThreadName(eventObject.getThreadName());
    }
}
