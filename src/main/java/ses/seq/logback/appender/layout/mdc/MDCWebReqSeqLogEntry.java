package ses.seq.logback.appender.layout.mdc;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ses.seq.logback.appender.layout.base.SeqLogEntry;

import java.text.Format;
import java.util.Map;

/**
 * Extended Seq Log Entry Model decorated to help with the serialization of a seq log event when using the
 * LogBack MDCInsertingServletFilter. It will include the web request info in seq logs
 * Contains the base seq params and basic logback params as well.
 *
 * Note: When using this, you need to use the MDCInsertingServletFilter in your project.
 * See <a href="https://logback.qos.ch/manual/mdc.html#mis">MDCInsertingServletFilter</a>
 * If you don't use/have a web.xml extend the class in your spring boot project as a filter
 */
@Getter
@Setter
public class MDCWebReqSeqLogEntry extends SeqLogEntry {

    @JsonProperty("ReqRemoteHost")
    private String remoteHost;

    @JsonProperty("ReqUserAgent")
    private String userAgent;

    @JsonProperty("ReqURI")
    private String reqUri;

    @JsonProperty("ReqURL")
    private String reqUrl;

    @JsonProperty("ReqQueryString")
    private String reqQueryString;

    @JsonProperty("ReqMethod")
    private String reqMethod;

    @JsonProperty("ReqForwardedFor")
    private String reqForwardedFor;


    /**
     * Constructor
     *
     * @param eventObject The logback event
     * @param dateFormat  Date format for timestamp
     */
    public MDCWebReqSeqLogEntry(ILoggingEvent eventObject, Format dateFormat) {
        super(eventObject, dateFormat);
        // Add the mdc values from the MDCInsertingServletFilter
        Map<String, String> mdc = eventObject.getMDCPropertyMap();
        this.setRemoteHost(mdc.get(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY));
        this.setUserAgent(mdc.get(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY));
        this.setReqUri(mdc.get(ClassicConstants.REQUEST_REQUEST_URI));
        this.setReqUrl(mdc.get(ClassicConstants.REQUEST_REQUEST_URL));
        this.setReqQueryString(mdc.get(ClassicConstants.REQUEST_QUERY_STRING));
        this.setReqMethod(mdc.get(ClassicConstants.REQUEST_METHOD));
        this.setReqForwardedFor(mdc.get(ClassicConstants.REQUEST_X_FORWARDED_FOR));
    }
}
