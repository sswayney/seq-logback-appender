package ses.seq.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.status.ErrorStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;

import java.text.Format;
import java.text.SimpleDateFormat;

 /**
 * Base Class responsible for formatting ILoggingEvents as json objects to be sent to seq server
 */
public class SeqLogEventLayout extends LayoutBase<ILoggingEvent> {
	protected final Format dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	private ObjectMapper jacksonObjectMapper = new ObjectMapper()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	@Override
	public String doLayout(ILoggingEvent event) {
		return this.getLogEntryJsonString(new SeqLogEntry(event, dateFormat));
	}

	/**
	 * Serializes a SeqLogEntry as a json string
	 * 
	 * @param logEntry
	 * @return json formatted String of the SeqLogEntry
	 */
	@NotNull
	protected String getLogEntryJsonString(SeqLogEntry logEntry) {
		String json = "";
		try {
			json = jacksonObjectMapper.writeValueAsString(logEntry);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			addStatus(new ErrorStatus("ERROR SERIALIZING OBJECT" + logEntry, this));
		} finally {
			json = json.replaceAll("\n", "");
		}
		return json + "\n";
	}
}

