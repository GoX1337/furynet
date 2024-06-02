package org.furynet.example.protocol;

import java.util.List;

public class Protocol {

    public static final List<Class<?>> PROTOCOL_EXAMPLE = List.of(
            NewConnection.class,
            MessageA.class,
            MessageB.class,
            MessageC.class,
            MessageD.class
    );
}
