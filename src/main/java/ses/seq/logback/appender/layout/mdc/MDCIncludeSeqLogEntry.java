package ses.seq.logback.appender.layout.mdc;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ses.seq.logback.appender.layout.base.SeqLogEntry;

import java.text.Format;
import java.util.HashMap;
import java.util.Map;

/**
 * Extended Seq Log Entry Model that will add all value pairs in MDC to the seq event json object sent to seq server
 */
@Getter
@Setter
public class MDCIncludeSeqLogEntry extends SeqLogEntry {

    @JsonIgnore
    private Map<String, String> mdcMap = new HashMap<>();

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return mdcMap;
    }

    /**
     * Constructor
     *
     * @param eventObject The logback event
     * @param dateFormat  Date format for timestamp
     */
    public MDCIncludeSeqLogEntry(ILoggingEvent eventObject, Format dateFormat) {
        super(eventObject, dateFormat);
        this.mdcMap = new HashMap<>(eventObject.getMDCPropertyMap());

    }
}
