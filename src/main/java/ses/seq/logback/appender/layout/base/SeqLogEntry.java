package ses.seq.logback.appender.layout.base;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ses.seq.logback.marker.ObjectAppendingMarker;

import java.text.Format;
import java.util.Date;

/**
 * Base Model decorated to help with the serialization of a seq log event
 * Contains the base seq params and basic logback params
 */
@Getter
@Setter
public class SeqLogEntry implements java.io.Serializable {

    @JsonProperty("@t")
    private String time;

    @JsonProperty("@m")
    private String message;

    @JsonProperty("@l")
    private String level;

    @JsonProperty("ThreadName")
    private String threadName;

    @JsonProperty("LogObject")
    private Object logObject;

    @JsonProperty("@x")
    private String exception;

    /**
     * Constructor
     * @param eventObject The logback event
     * @param dateFormat Date format for timestamp
     */
    public SeqLogEntry(ILoggingEvent eventObject, Format dateFormat) {
        Date date = new Date(eventObject.getTimeStamp());
        this.setTime(dateFormat.format(date));
        this.setMessage(eventObject.getFormattedMessage());
        this.setLevel(eventObject.getLevel().toString());
        this.setThreadName(eventObject.getThreadName());
        try {
            this.setLogObject(((ObjectAppendingMarker)eventObject.getMarker()).getObject());
        } catch(Exception e) {}
        try {
            String stackTrace = ThrowableProxyUtil.asString(eventObject.getThrowableProxy());
            if (stackTrace != null && !stackTrace.isEmpty())
                this.setException(stackTrace);
        } catch(Exception e) {}
    }
}
