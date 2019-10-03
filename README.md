# seq-logback-appender

Send logback log events to a seq server

[![Build Status](https://travis-ci.com/sswayney/seq-logback-appender.svg?branch=master)](https://travis-ci.com/sswayney/seq-logback-appender)
[![](https://jitpack.io/v/sswayney/seq-logback-appender.svg)](https://jitpack.io/#sswayney/seq-logback-appender)


## Install
#### Maven
Add Repo
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Add Dependency
```xml
	<dependency>
	    <groupId>com.github.sswayney</groupId>
	    <artifactId>seq-logback-appender</artifactId>
	    <version>VERSION</version>
	</dependency>
```
#### Gradle
Add Repo
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add Dependency
```xml
	dependencies {
	        implementation 'com.github.sswayney:seq-logback-appender:VERSION'
	}
```

## Usage

Add the required properties to your application.yaml file. Note, batchCount defaults to 1.

<small>Also Note (Issue #12) if you set a large batch count then applications that log infrequently may be holding messages in the buffer for an extended period of time. A scheduled job that runs once per day for example may have finished processing, but you won't see the final messages until the job starts the following day.</small>
```yaml
seq:
  batchCount: 5
  apiKey: YOURAPIKEY
  server: http://URLTOYOURSEQSERVER
  port: 80
```


For each project, include the seq logback settings file in your main logback settings file, and you must include a project name property which will be used for the logging filename
```xml
    <property name="PROJECT_NAME" value="my-service"/>
    <include resource="seq-logback-settings.xml" />
```

Then, you can add the appenders to your profiles. "SEQ" sends logs to server and "FILE" logs to a file under applogs/PROJECT_NAME.log and CONSOLE to the console
```xml
    <springProfile name="dev">
        <root level="INFO">
            <appender-ref ref="SEQ" />
            <appender-ref ref="FILE" />
            <appender-ref ref="CONSOLE" />
        </root>
    </springProfile>
```
<<<<<<< HEAD

To use the logs in Java you can do something like this:

```java
    import lombok.extern.slf4j.Slf4j;
    import static ses.seq.logback.marker.ObjectAppendingMarker.append;
    
    @Slf4j
    public class MyClass {
    
        public void myMethod(String input, JsonPojo pojo) {
            //basic logs message
            log.info("Entering test method, input: {}", input);
    
            //json object logging for debugging
            log.info(append(pojo), "Object state");
    
            try {
                Integer.parseInt("FAIL");
            } catch (NumberFormatException e) {
                //exception logging
                log.error("Exception in complex method", e);
            }
        }
    }
```

Done... It should log basic messages.

## Adding more data to log
The base Seq Log Event Layout SeqLogEventLayout will map the basic logging data to the base SeqLogEntry
object and send it to the specified seq server. To add more, extend SeqLogEventLayout and SeqLogEntry to add more data.
Then set the seq layout class to use in your logback settings file.

#### Example:

1: Extend SeqLogEntry to add your fields. Note, I'm using lombok to create my getter/setters. You could do the same or create your own getter setters.
```java
@Getter
@Setter
public class MyCustomSeqLogEntry extends SeqLogEntry {

    @JsonProperty("User")
    private String user;

    @JsonProperty("Method")
    private String method;

    @JsonProperty("Pid")
    private String pid;

    @JsonProperty("Query")
    private String query;

    @JsonProperty("Referer")
    private String referer;

    @JsonProperty("IP")
    private String ip;

    @JsonProperty("Url")
    private String url;

    @JsonProperty("Token")
    private String token;

    /**
     * Constructor
     * @param eventObject The logback event
     * @param dateFormat Date format for timestamp
     */
    public MyCustomSeqLogEntry(ILoggingEvent eventObject, Format dateFormat) {
        super(eventObject, dateFormat);
        Map<String, String> mdc = eventObject.getMDCPropertyMap();
        this.setUser(mdc.get(Constants.LOGGED_ON_USER_NAME));
        this.setMethod(mdc.get(Constants.REQUEST_METHOD));
        this.setPid(mdc.get(Constants.ADMIN_PID));
        this.setQuery(mdc.get(Constants.REQUEST_QUERY));
        this.setReferer(mdc.get(Constants.REQUEST_REFERER));
        this.setIp(mdc.get(Constants.REQUEST_IP_ADDRESS));
        this.setUrl(mdc.get(Constants.REQUEST_URL));
        this.setToken(mdc.get(Constants.AUTH_TOKEN));
    }
}

```

2: Extend SeqLogEventLayout to use your new MyCustomSeqLogEntry class
```java
public class MyCustomSeqLogEventLayout extends SeqLogEventLayout {

    @Override
    public String doLayout(ILoggingEvent event) {
        return this.getLogEntryJsonString(new MyCustomSeqLogEntry(event, this.dateFormat));
    }
}
```

3: Add SEQ_LAYOUT property to your base logback settings xml file to use your new extended layout class.
Make sure the namespace to your layout is correct.
```xml
    <property scope="context" name="SEQ_LAYOUT" value="com.your.namespace.MyCustomSeqLogEventLayout"/>
    <include resource="seq-logback-settings.xml" />
```
Done. Now these new fields will be added to all your logging events. 


## Logging basic request info with your log using LogBack's MDCInsertingServletFilter
Logback has filters to add basic request info to your MDC. You can have that added to your logs too. Much like above, but instead of extending the basic SeqLogEntry, you would just extend MDCWebReqSeqLogEntry. And instead of extending SeqLogEventLayout you would extend MDCWebReqSeqLogLayout. But first you need to add LogBack's MDCInsertingServletFilter to your project. 

1: Add MDCInsertingServletFilter to your project. See https://logback.qos.ch/manual/mdc.html#mis if you use a web.xml. If not just do the below.
```java
import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class CpeMDCInsertingServletFilter extends MDCInsertingServletFilter {
}
```

2: Extend MDCWebReqSeqLogEntry to add your fields. Note, I'm using lombok to create my getter/setters. You could do the same or create your own getter setters.
```java
@Getter
@Setter
public class MyCustomSeqLogEntry extends MDCWebReqSeqLogEntry { 

    // add your own custom fields here if you would like
    
    /**
     * Constructor
     * @param eventObject The logback event
     * @param dateFormat Date format for timestamp
     */
    public MyCustomSeqLogEntry(ILoggingEvent eventObject, Format dateFormat) {
        super(eventObject, dateFormat);
	// set any custom fields here
    }
}
```

3: Extend MDCWebReqSeqLogLayout to use your new MyCustomSeqLogEntry class
```java
public class MyCustomSeqLogEventLayout extends MDCWebReqSeqLogLayout {

    @Override
    public String doLayout(ILoggingEvent event) {
        return this.getLogEntryJsonString(new MyCustomSeqLogEntry(event, this.dateFormat));
    }
}
```

4: Add SEQ_LAYOUT property to your base logback settings xml file to use your new extended layout class.
Make sure the namespace to your layout is correct.
```xml
    <property scope="context" name="SEQ_LAYOUT" value="com.your.namespace.MyCustomSeqLogEventLayout"/>
    <include resource="seq-logback-settings.xml" />
```


## Tips
Remember to use debug="true" in main logback configuration file when trying to set up.

