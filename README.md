# furynet

[kryonet](https://github.com/EsotericSoftware/kryonet) clone but using [Netty](https://github.com/netty/netty) and [Apache Fury](https://github.com/apache/incubator-fury) for serialization/deserialization

### Protocol
```java
public static final List<Class<?>> PROTOCOL_EXAMPLE = List.of(
            ClientConnection.class,
            ClientDisconnection.class,
            Request.class,
            Response.class,
            BroadcastMessage.class
);
```

### Client
```java
 Client client = Client.builder()
                .hostname("127.0.0.1")
                .tcpPort(42000)
                .protocol(Protocol.PROTOCOL_EXAMPLE)
                .register(ClientConnection.class, (connection, msg) -> logger.info("New player connected"))
                .register(ClientDisconnection.class, (connection, msg) -> logger.info("A player disconnected"))
                .register(Response.class, (connection, msg) -> logger.info("Received response msg {}", msg))
                .register(BroadcastMessage.class, (connection, msg) -> logger.info("Received broadcasted msg {}", msg))
                .build();
client.start();
client.sendTcp(new Request("Hello there"));
```

### Server
```java
Server.builder()
        .tcpPort(42000)
        .protocol(Protocol.PROTOCOL_EXAMPLE)
        .register(ServerEvent.NEW_CLIENT_CONNECTION, (connection) -> {
            connection.sendToAllExceptTcp(new ClientConnection());
        })
        .register(ServerEvent.CLIENT_DISCONNECTED, (connection) -> {
            connection.sendToAllTcp(new ClientDisconnection());
        })
        .register(Request.class, (connection, msg) -> {
            connection.sentTcp(new Response());
        })
        .register(BroadcastMessage.class, (connection, msg) -> {
            connection.sendToAllExceptTcp(msg);
        })
        .build()
        .start();
```
