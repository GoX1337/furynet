package org.furynet.example.protocol;

public class MessageB extends Message {

    private final boolean z;

    public MessageB(int x, int y, boolean z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public String toString() {
        return "MessageB{" +
                "z=" + z +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
