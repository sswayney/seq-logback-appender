package ses.seq.logback.appender.layout.mdc;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ses.seq.logback.appender.layout.base.SeqLogEventLayout;

/**
 * Layout extends base seq layout and includes all MDC key values in JSON object sent to seq server
 */
public class MDCIncludeSeqLogLayout extends SeqLogEventLayout {
    @Override
    public String doLayout(ILoggingEvent event) {
        return this.getLogEntryJsonString(new MDCIncludeSeqLogEntry(event, this.dateFormat));
    }
}
