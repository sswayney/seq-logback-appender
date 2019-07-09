package ses.seq.logback.appender.base;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Test;
import ses.seq.logback.appender.layout.base.SeqLogEventLayout;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class SeqLogEventLayoutTest {
    @Test public void testDoLayoutMethod() {
        long currentTimeMillis = System.currentTimeMillis();
        Format dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = new Date(currentTimeMillis);
        String dateStr = dateFormat.format(date);
        String expected = "{\"@t\":\"" + dateStr + "\",\"@m\":\"test\",\"@l\":\"INFO\",\"ThreadName\":\"test-thread\"}\n";

        SeqLogEventLayout classUnderTest = new SeqLogEventLayout();
        LoggingEvent loggingEvent = new LoggingEvent();
        loggingEvent.setMessage("test");
        loggingEvent.setLevel(Level.INFO);
        loggingEvent.setThreadName("test-thread");
        loggingEvent.setTimeStamp(currentTimeMillis);

        String layout = classUnderTest.doLayout(loggingEvent);
        assertEquals("doLayout should return formatted string", expected, layout);
    }
}
