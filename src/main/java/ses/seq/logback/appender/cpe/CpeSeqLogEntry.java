package ses.seq.logback.appender.cpe;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.conversantmedia.cpeui.shared.constants.Constants;
import com.conversantmedia.cpeui.shared.util.seq.SeqLogEntry;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.text.Format;
import java.util.Map;

/**
 * Cpe log event class to help format our json event before posting to seq
 */
@Getter
@Setter
public class CpeSeqLogEntry extends SeqLogEntry {

    @JsonProperty("User")
    private String user;

    @JsonProperty("Method")
    private String method;

    @JsonProperty("Pid")
    private String pid;

    @JsonProperty("Query")
    private String query;

    @JsonProperty("Referer")
    private String referer;

    @JsonProperty("IP")
    private String ip;

    @JsonProperty("Url")
    private String url;

    @JsonProperty("Token")
    private String token;

    /**
     * Constructor
     * @param eventObject The logback event
     * @param dateFormat Date format for timestamp
     */
    public CpeSeqLogEntry(ILoggingEvent eventObject, Format dateFormat) {
        super(eventObject, dateFormat);
        Map<String, String> mdc = eventObject.getMDCPropertyMap();
        this.setUser(mdc.get(Constants.LOGGED_ON_USER_NAME));
        this.setMethod(mdc.get(Constants.REQUEST_METHOD));
        this.setPid(mdc.get(Constants.ADMIN_PID));
        this.setQuery(mdc.get(Constants.REQUEST_QUERY));
        this.setReferer(mdc.get(Constants.REQUEST_REFERER));
        this.setIp(mdc.get(Constants.REQUEST_IP_ADDRESS));
        this.setUrl(mdc.get(Constants.REQUEST_URL));
        this.setToken(mdc.get(Constants.AUTH_TOKEN));
    }
}
