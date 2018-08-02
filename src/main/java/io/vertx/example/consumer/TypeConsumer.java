package io.vertx.example.consumer;

import io.vertx.example.dto.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoavstern on 01/08/2018.
 */

@Service

public class TypeConsumer extends AbstractConsumer {

    public final static String TYPE = "type";

    @Autowired
    @Qualifier("typeCounter")
    public  ConcurrentHashMap<String , Counter> typeCounter;

    @Override
    public String getAddress() {
        return TYPE;
    }

    @Override
    public ConcurrentHashMap<String, Counter> getCounterMap() {
        return typeCounter;
    }


}