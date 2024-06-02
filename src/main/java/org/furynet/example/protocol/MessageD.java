package org.furynet.example.protocol;

public class MessageD {

    private final String msg;

    public MessageD(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MessageD{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
