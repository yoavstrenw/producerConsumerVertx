package io.vertx.example;


import io.vertx.core.Vertx;
import io.vertx.example.consumer.DataConsumer;
import io.vertx.example.consumer.TypeConsumer;
import io.vertx.example.controler.ServerVerticle;
import io.vertx.example.producer.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

  @Autowired
  private TypeConsumer typeConsumer;
  @Autowired
  private ServerVerticle serverVerticle;
  @Autowired
  private DataConsumer dataConsumer;
  @Autowired
  private InputHandler inputHandler;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @PostConstruct
  public void deployVerticle() {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(typeConsumer);
    vertx.deployVerticle(dataConsumer);
    vertx.deployVerticle(serverVerticle);
    vertx.deployVerticle(inputHandler);

  }


}


//    Vertx.vertx().deployVerticle(staticServer);

