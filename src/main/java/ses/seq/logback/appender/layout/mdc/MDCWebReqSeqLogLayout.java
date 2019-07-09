package ses.seq.logback.appender.layout.mdc;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ses.seq.logback.appender.layout.base.SeqLogEventLayout;

/**
 * Layout extends base seq layout and includes the basic web request info placed in the MDC via
 * the MDCInsertingServletFilter in json object sent to seq server.
 *
 * Note: When using this, you need to use the MDCInsertingServletFilter in your project.
 * See <a href="https://logback.qos.ch/manual/mdc.html#mis">MDCInsertingServletFilter</a>
 * If you don't use/have a web.xml extend the class in your spring boot project as a filter
 */
public class MDCWebReqSeqLogLayout extends SeqLogEventLayout {
    @Override
    public String doLayout(ILoggingEvent event) {
        return this.getLogEntryJsonString(new MDCWebReqSeqLogEntry(event, this.dateFormat));
    }
}
