# producerConsumerVertx

hi , 
I choose to use vertx (this is the only framework i know , and not mastering , did a poc and a project). 

# producer 
run the generator (process ) , I am reading from this process output (blocking, i didn’t find a way to listen to this stream async , I am not mastering vertx and didn’t find how to do it in 2 hrs looking )
flow
1.validation 
  its a valid object 
  two similar object in the same time frame are duplication
2.move the good events to consumers via event bus
  typeCount
  dataCount 


# consumer
listen to event bus async 
persist the messages (to concurrent hash map increment count times )

# Api 
rest api via vertx which goes and query the concurrent hash map for count 

