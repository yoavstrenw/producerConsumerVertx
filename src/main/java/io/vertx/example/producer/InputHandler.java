package io.vertx.example.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.consumer.DataConsumer;
import io.vertx.example.consumer.TypeConsumer;
import io.vertx.example.dto.PandaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wisdom.framework.vertx.AsyncInputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yoavstern on 31/07/2018.
 */
@Service
@Slf4j
public class InputHandler extends AbstractVerticle {
    //only one thread does writing/reading
    final Map<String, PandaEvent> currentTimestampEventsHashMap = new HashMap<>();
    final private ObjectMapper om = new ObjectMapper();
    @Value("${cmd}")
    String command;
    @Value("${supportDuplication:false}")
    private boolean supportDuplication;
    private PandaEvent lastPandaEvent = PandaEvent.builder().timestamp(0).build();


    public void start() throws Exception {
        Runtime r = Runtime.getRuntime();
        Process p = r.exec(command);
        AsyncInputStream asyncInputStream = new AsyncInputStream(vertx, vertx.nettyEventLoopGroup(), p.getInputStream());
        asyncInputStream.handler(buffer ->
                lineHandler(buffer.getString(0, buffer.length() - 1))
        );
    }

    private void lineHandler(String line) {
        PandaEvent currentPandaEvent = createPandaEvent(line);
        if (currentPandaEvent == null) {
            return;
        }
        if (supportDuplication && isDuplication(currentPandaEvent))
            return;

        vertx.eventBus().send(TypeConsumer.TYPE,
                currentPandaEvent.getEventType());
        vertx.eventBus().send(DataConsumer.DATA,
                currentPandaEvent.getData());
    }


    private boolean isDuplication(PandaEvent currentPandaEvent) {
        if (lastPandaEvent.getTimestamp() == currentPandaEvent.getTimestamp()) {
            final PandaEvent o = currentTimestampEventsHashMap.get(currentPandaEvent.getData());
            if (currentPandaEvent.equals(o)) {
                log.info("duplication :" + currentPandaEvent);
                return true;
            } else {
                currentTimestampEventsHashMap.put(currentPandaEvent.getData(), currentPandaEvent);
            }
        } else {
            currentTimestampEventsHashMap.clear();
            currentTimestampEventsHashMap.put(currentPandaEvent.getData(), currentPandaEvent);
        }
        //only one event does this , so no sync problem
        lastPandaEvent = currentPandaEvent;
        return false;
    }

    private PandaEvent createPandaEvent(String line) {
        try {
            return om.readValue(line, PandaEvent.class);
        } catch (IOException e) {
            return null;
        }
    }

//     blocking
//    public void start11() throws Exception {
//        Runtime r = Runtime.getRuntime();
//        String line;
//        Process p = r.exec(command);
//        BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        while ((line = is.readLine()) != null)
//            try {
//                lineHandler(line);
//            } catch (Exception ex) {
//                log.error(ex);
//            }
//    }

}



