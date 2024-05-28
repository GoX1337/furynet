# furynet

[kryonet](https://github.com/EsotericSoftware/kryonet) clone but using [Netty](https://github.com/netty/netty) and [Apache Fury](https://github.com/apache/incubator-fury) for serialization/deserialization

### Client
```java
Client client = Client.builder()
                .hostname("127.0.0.1")
                .tcpPort(42000)
                .register(Message.class, (ctx, o) -> System.out.println("Message consumer: " + o))
                .register(Ping.class, (ctx, o) -> System.out.println("Ping consumer: " + o))
                .build();

client.start();

client.sendTcp(new Ping("Hello there"));
```

### Server (WIP)
```java
Server.builder()
        .tcpPort(42000)
        .register(Message.class)
        .register(Ping.class)
        .build()
        .start();
```
