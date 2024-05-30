package org.furynet.example.protocol;

import java.util.Objects;

public class Ping {

    private String message;

    public Ping(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Ping{" +
                "message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ping ping = (Ping) o;
        return Objects.equals(message, ping.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
