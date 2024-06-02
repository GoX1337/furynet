package org.furynet.example.protocol;

public class MessageA extends Message {

    private final String z;

    public MessageA(int x, int y, String z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public String toString() {
        return "MessageA{" +
                "z='" + z + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
