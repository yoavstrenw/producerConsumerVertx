package io.vertx.example.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.example.dto.Counter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoavstern on 01/08/2018.
 */
@Slf4j

public abstract class AbstractConsumer extends AbstractVerticle {

    public abstract String getAddress();

    public abstract ConcurrentHashMap<String, Counter> getCounterMap();

    @Override
    public void start() {
        vertx.eventBus().consumer(getAddress(), message ->
                handleEvent(message)
        );
    }

    private void handleEvent(Message<Object> message) {
        final String body = (String) message.body();
        log.info(getAddress()+":"+body);
        final Counter c = getCounterMap().get(body);
        if (c != null) {
            c.inc();
        } else {
            getCounterMap().put(body, new Counter());
        }
    }


}
