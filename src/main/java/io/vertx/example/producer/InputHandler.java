package io.vertx.example.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.consumer.DataConsumer;
import io.vertx.example.consumer.TypeConsumer;
import io.vertx.example.dto.PandaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yoavstern on 31/07/2018.
 */
@Service
@Slf4j
public class InputHandler extends AbstractVerticle {
    final Map<String, PandaEvent> currentTimestampEventsHashMap = new HashMap();
    final private ObjectMapper om = new ObjectMapper();
    @Value("${cmd}")
    String command;
    private PandaEvent lastPandaEvent = PandaEvent.builder().timestamp(0).build();


    public void start() throws Exception {

        log.info("starting ");
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Runtime r = Runtime.getRuntime();
                String line;
                Process p = r.exec(command);
                log.debug("after process execution ");
                BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = is.readLine()) != null) {
                    log.info("handling  " + line);
                    emitter.onNext(line);
                }
                emitter.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.computation()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                lineHandler(s);
            }

            @Override
            public void onError(Throwable e) {
                log.error(e.getMessage());
            }

            @Override
            public void onComplete() {
                log.info("ended processing .");
            }
        });


    }


    private void lineHandler(String line) {
        PandaEvent currentPandaEvent = createPandaEvent(line);
        if (currentPandaEvent == null) {
            return;
        }
        if (isDuplication(currentPandaEvent))
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
        lastPandaEvent = currentPandaEvent;
        return false;
    }

    private PandaEvent createPandaEvent(String line) {
//        final JsonObject entries = new JsonObject(line);
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



