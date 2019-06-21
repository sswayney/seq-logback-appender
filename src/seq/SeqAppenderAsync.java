package com.conversantmedia.cpeui.shared.util.seq;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.InfoStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * SeqAppenderAsync handles logback logging events and posts them to a seq server inject end point
 * Given the configurations passed to the class, the post may contain multiple events
 *
 * You must at least pass the server. If your seq server requires an api token you should specify
 * that in the config. If no batch count is passed, no batch posting of log events will happen.
 * It will post one at a time.
 */
public class SeqAppenderAsync extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private final String SEQ_API_TOKEN_HEADER_NAME = "X-Seq-ApiKey";
    private final String INGEST_ENDPOINT = "/api/events/raw?clef";
    private final List<String> eventList = new ArrayList<>();

    @Getter @Setter private Layout<ILoggingEvent> layout;

    // Parameters configured in logback config xml
    @Getter @Setter private String apiKey = "";
    @Getter @Setter private String server = "";
    @Getter @Setter private String port = "";
    @Getter @Setter private int eventBatchCount = 1;

    private String getServerIngestUrl(){
        return port.equals("") ? server + INGEST_ENDPOINT : server + ":" + port + INGEST_ENDPOINT;
    }

    @Override
    public void start() {
        if (this.layout == null) {
            addStatus(new ErrorStatus("No layout set for the appender named \"" + name + "\".", this));
            return; // will not start
        }

        if (this.server.equals("")) {
            addStatus(new ErrorStatus("No server set for appender named \"" + name + "\".", this));
            return; // will not start
        }

        addStatus(new InfoStatus("Seq Server Ingestion Url: " + this.getServerIngestUrl(),this));
        addStatus(new InfoStatus("Seq Server Api Key: " + this.apiKey,this));
        addStatus(new InfoStatus("Seq Server Batch Count: " + this.eventBatchCount,this));

        super.start();
    }

    /**
     * Main entry point for our use case
     * @param eventObject
     */
    @Override
    protected void append(ILoggingEvent eventObject) {
        addLogEventToLogList(eventObject);
        if (shouldPostLogs()) postLogs();
    }

    /**
     * Serializes the log event as an json string and adds it to the log list for later processing
     * @param eventObject
     */
    private void addLogEventToLogList(ILoggingEvent eventObject) {
        String json = this.layout.doLayout(eventObject);
        if (json.equals("")){
            addStatus(new ErrorStatus("Empty Json Log: " + json,this));
        }
        eventList.add(json + "\n");
    }

    /**
     * Returns true if we are ready to post logs
     * @return boolean
     */
    private boolean shouldPostLogs() {
        return eventList.size() >= eventBatchCount;
    }

    /**
     * Posts the current logs in the log list to the seq ingestion end point
     */
    private void postLogs() {
        String bodyString = getPostBody();
        eventList.clear();
        CloseableHttpAsyncClient httpClient = HttpAsyncClientBuilder.create()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectTimeout(2000)
                                .setSocketTimeout(10000)
                                .setConnectionRequestTimeout(2000)
                                .build())
                .build();

        HttpPost post = null;
        try {
            StringEntity entity = new StringEntity(bodyString);
            post = new HttpPost(getServerIngestUrl()) {{
                setEntity(entity);
                setHeader(SEQ_API_TOKEN_HEADER_NAME, apiKey);
            }};
        } catch (UnsupportedEncodingException e) {
            addStatus(new ErrorStatus("Error creating header for postLogs to seq : " + e.getMessage(),this));
            addStatus(new ErrorStatus("Post body: " + bodyString,this));
            return;
        }

        httpClient.start();
        try {
            Future<HttpResponse> execute = httpClient.execute(post, null);
            HttpResponse response = execute.get();
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201) {
                addStatus(new ErrorStatus("Seq Post Status: " + response.getStatusLine().getStatusCode(), this));
                addStatus(new ErrorStatus("Post body: " + bodyString,this));
            }
        } catch (Exception e) {
            addStatus(new ErrorStatus("Error while reading response from Seq",this));
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                addStatus(new ErrorStatus("Error on closing httpClient in SeqAppender" + e.getMessage(),this));
            }
        }
    }

    /**
     * Returns the current log list formatted to be posted to seq server
     * @return formatted event string
     */
    @NotNull
    private String getPostBody() {
        StringBuilder bodyString = new StringBuilder();
        for(String eventStr : eventList) {
            bodyString.append(eventStr).append("\n");
        }
        return bodyString.toString();
    }
}
