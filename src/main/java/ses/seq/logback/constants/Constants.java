package ses.seq.logback.constants;

/**
 * Constants used throughout the library
 */
public class Constants {
    public static final String SEQ_API_TOKEN_HEADER_NAME = "X-Seq-ApiKey";
    public static final String INGEST_ENDPOINT = "/api/events/raw?clef";
    public static final int DEFAULT_EVENT_BATCH_COUNT = 1;

    public static final String APP_HOST_NAME = "appHostName";
    public static final String BUILD_VERSION = "buildVersion";

    public static final String REQUEST_REFERER = "requestReferer";
    public static final String REQUEST_AUTH_TOKEN = "requestAuthToken";
}
