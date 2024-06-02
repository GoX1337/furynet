package org.furynet.example.util;

import java.util.Random;

public class RandomUtils {

    public static int randNum() {
        return new Random().nextInt((100 - 1) + 1) + 1;
    }
}
