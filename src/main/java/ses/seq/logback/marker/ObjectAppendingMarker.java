package ses.seq.logback.marker;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.logstash.logback.argument.StructuredArguments;
import net.logstash.logback.marker.LogstashMarker;
import net.logstash.logback.marker.SingleFieldAppendingMarker;

import java.io.IOException;
import java.util.Objects;

public class ObjectAppendingMarker extends SingleFieldAppendingMarker {

    public static final String MARKER_NAME = SingleFieldAppendingMarker.MARKER_NAME_PREFIX + "OBJECT";

    /**
     * The object to write as the field's value.
     * Can be a {@link String}, {@link Number}, array, or some other object that can be processed by an {@link ObjectMapper}
     */
    private final Object object;

    public static LogstashMarker append(String fieldName, Object object) {
        return new ObjectAppendingMarker(fieldName, object);
    }

    public ObjectAppendingMarker(String fieldName, Object object) {
        super(MARKER_NAME, fieldName);
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    protected void writeFieldValue(JsonGenerator generator) throws IOException {
        generator.writeObject(object);
    }

    @Override
    public Object getFieldValue() {
        return StructuredArguments.toString(object);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ObjectAppendingMarker)) {
            return false;
        }

        ObjectAppendingMarker other = (ObjectAppendingMarker) obj;
        return Objects.equals(this.object, other.object);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + super.hashCode();
        result = prime * result + (this.object == null ? 0 : this.object.hashCode());
        return result;
    }
}