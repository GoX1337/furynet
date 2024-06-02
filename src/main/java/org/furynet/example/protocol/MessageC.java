package org.furynet.example.protocol;

public class MessageC extends Message {

    private final float z;

    public MessageC(int x, int y, float z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public String toString() {
        return "MessageC{" +
                "z=" + z +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
