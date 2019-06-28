package ses.seq.logback.appender.cpe;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.conversantmedia.cpeui.shared.util.seq.SeqLogEventLayout;

/**
 * Cpe Class responsible for formatting ILoggingEvents as json objects to be sent to seq server
 */
public class CpeSeqLogEventLayout extends SeqLogEventLayout {

    @Override
    public String doLayout(ILoggingEvent event) {
        return this.getLogEntryJsonString(new CpeSeqLogEntry(event, this.dateFormat));
    }
}
