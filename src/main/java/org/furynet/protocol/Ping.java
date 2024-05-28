package org.furynet.protocol;

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
}
