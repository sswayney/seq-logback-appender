package ses.seq.logback.appender.mdc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Test;
import ses.seq.logback.appender.layout.mdc.MDCIncludeSeqLogLayout;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MDCIncludeSeqLogLayoutTest {
    @Test public void testDoLayoutMethod() {
        long currentTimeMillis = System.currentTimeMillis();
        Format dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = new Date(currentTimeMillis);
        String dateStr = dateFormat.format(date);
        Map<String, String> mdc = new HashMap<>();
        mdc.put("TestKey1", "TestValue1");
        mdc.put("TestKey2", "TestValue2");
        String expected = "{\"@t\":\"" + dateStr + "\",\"@m\":\"test\",\"@l\":\"INFO\",\"ThreadName\":\"test-thread\",\"TestKey2\":\"TestValue2\",\"TestKey1\":\"TestValue1\"}\n";


        MDCIncludeSeqLogLayout classUnderTest = new MDCIncludeSeqLogLayout();
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
