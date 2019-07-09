package ses.seq.logback.appender.mdc;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Test;
import ses.seq.logback.appender.layout.mdc.MDCWebReqSeqLogLayout;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MDCWebReqSeqLogLayoutTest {
    @Test public void testDoLayoutMethod() {
        long currentTimeMillis = System.currentTimeMillis();
        Format dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = new Date(currentTimeMillis);
        String dateStr = dateFormat.format(date);
        Map<String, String> mdc = new HashMap<>();
        mdc.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, "remoteHost");
        mdc.put(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY, "userAgent");
        mdc.put(ClassicConstants.REQUEST_REQUEST_URI, "uri");
        mdc.put(ClassicConstants.REQUEST_REQUEST_URL, "url");
        mdc.put(ClassicConstants.REQUEST_QUERY_STRING, "queryString");
        mdc.put(ClassicConstants.REQUEST_METHOD, "PUT");
        mdc.put(ClassicConstants.REQUEST_X_FORWARDED_FOR, "forwardedFor");
        String expected = "{\"@t\":\"" + dateStr + "\",\"@m\":\"test\",\"@l\":\"INFO\",\"ThreadName\":\"test-thread\",\"ReqRemoteHost\":\"remoteHost\",\"ReqUserAgent\":\"userAgent\",\"ReqURI\":\"uri\",\"ReqURL\":\"url\",\"ReqQueryString\":\"queryString\",\"ReqMethod\":\"PUT\",\"ReqForwardedFor\":\"forwardedFor\"}\n";


        MDCWebReqSeqLogLayout classUnderTest = new MDCWebReqSeqLogLayout();
        LoggingEvent loggingEvent = new LoggingEvent();
        loggingEvent.setMessage("test");
        loggingEvent.setLevel(Level.INFO);
        loggingEvent.setThreadName("test-thread");
        loggingEvent.setTimeStamp(currentTimeMillis);
        loggingEvent.setMDCPropertyMap(mdc);

        String layout = classUnderTest.doLayout(loggingEvent);
        assertEquals("doLayout should return formatted string", expected, layout);
    }
}
