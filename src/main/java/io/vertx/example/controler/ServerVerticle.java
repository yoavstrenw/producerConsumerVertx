package io.vertx.example.controler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.example.dto.Counter;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Created by yoavstern on 01/08/2018.
 */
@Component
public class ServerVerticle extends AbstractVerticle {

    @Autowired
    @Qualifier("dataCounter")
    public ConcurrentHashMap<String, Counter> dataCounter;
    @Autowired
    @Qualifier("typeCounter")
    public ConcurrentHashMap<String, Counter> typeCounter;

    private void getTypeCount(RoutingContext rc) {
        final String id = rc.request().getParam("id");
        rc.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(typeCounter.get(id)));
    }

    private void getDataCount(RoutingContext rc) {
        final String id = rc.request().getParam("id");
        rc.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(dataCounter.get(id)));
    }

    @Override
    public void start(Future<Void> fut) throws Exception {
        super.start();

        Router router = Router.router(vertx);
        router.get("/v1/type/count/:id").handler(this::getTypeCount);
        router.get("/v1/data/count/:id").handler(this::getDataCount);
        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );

    }

}
