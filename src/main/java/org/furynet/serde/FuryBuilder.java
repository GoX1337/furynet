package org.furynet.serde;

import org.apache.fury.Fury;
import org.apache.fury.ThreadSafeFury;
import org.apache.fury.config.Language;

import java.util.List;

public class FuryBuilder {

    public static ThreadSafeFury buildFurySerde(List<Class<?>> registeredClasses) {
        ThreadSafeFury fury = Fury.builder()
                .withLanguage(Language.JAVA)
                .requireClassRegistration(true)
                .buildThreadSafeFury();
        for (Class<?> clazz : registeredClasses) {
            fury.register(clazz);
        }
        return fury;
    }
}
