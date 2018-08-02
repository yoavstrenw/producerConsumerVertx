package io.vertx.example.dto;

import lombok.*;

/**
 * Created by yoavstern on 01/08/2018.
 */
@Getter
@Setter
public class Counter {
    long count= 1;
    public void inc(){
        count++;
    }
}
