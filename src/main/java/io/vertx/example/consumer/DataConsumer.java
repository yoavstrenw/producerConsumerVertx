package io.vertx.example.consumer;

import io.vertx.example.dto.Counter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Created by yoavstern on 01/08/2018.
 */
@Service
@Slf4j
public class DataConsumer extends AbstractConsumer {

    public final static String DATA = "data";

    @Autowired
    @Qualifier("dataCounter")
    public ConcurrentHashMap<String , Counter> dataCounter;

    @Override
    public String getAddress() {
        return DATA;
    }

    @Override
    public ConcurrentHashMap<String, Counter> getCounterMap() {
        return dataCounter;
    }

}
