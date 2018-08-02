package io.vertx.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * Created by yoavstern on 01/08/2018.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PandaEvent implements Serializable {
    @JsonProperty("event_type")
    private String eventType;
    private String data;
    private long timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PandaEvent)) return false;

        PandaEvent that = (PandaEvent) o;

        if (timestamp != that.timestamp) return false;
        if (!eventType.equals(that.eventType)) return false;
        return data.equals(that.data);

    }

    @Override
    public int hashCode() {
        int result = eventType.hashCode();
        result = 31 * result + data.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
}



