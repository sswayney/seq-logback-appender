package ses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static ses.seq.logback.marker.ObjectAppendingMarker.append;

/**
 * Dev console to test code. Is removed from build.
 * Gradle property remove_dev_console_from_build needs to be set to false to use.
 */
@Slf4j
@SpringBootApplication
public class DevConsole implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(DevConsole.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.debug("This is a debug message");
        log.info("This is an INFO test and {}", "this is a string");
        log.info("The thread name is {ThreadName}"); // ThreadName is in seq
        log.info(append("Log Object String"), "Object state");
        log.info(append("Log Object String 2"), "Object state is {LogObject}");
        log.warn("This is a warning");

        try {
            throw new Exception("Test Error: testing an error.");
        } catch(Exception e) {
            log.error("We got an error here!", e);
        }
    }
}
