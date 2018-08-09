# *ProducerConsumerVertx*


## Running the project 
1.  inside the application.properties file change configuration : 

    1. cmd = location of execuatable  in the running env 
    2. supportDuplication , choose if the system should be idempotent (based on assumption if events e1,e2 in t1 and there timestamp should be equal  )
     
2. build the project (from the directory pom.xml do mvn clean install )

3. do java -jar producerConsumerVertx-1.2.4.RELEASE.jar 

## Rest Api
  rest api via vertx which goes and query the concurrent hash map for count  can be added by typing(GET method ... ) 

1.   http://localhost:8080/v1/type/count/:id  e.g: http://localhost:8080/v1/type/count/foo
2.   http://localhost:8080/v1/data/count/:id e.g: http://localhost:8080/v1/data/count/amet 


