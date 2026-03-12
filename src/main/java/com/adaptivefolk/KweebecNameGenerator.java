package com.adaptivefolk;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class KweebecNameGenerator {

    private static final List<String> NAMES = new ArrayList<>();
    private static final Random RANDOM = new Random();

    static {
        NAMES.add("Lightroot");
        NAMES.add("Bulbstalk");
        NAMES.add("Treebeard");
        NAMES.add("Glowfern");
        NAMES.add("Thornwhisper");
        NAMES.add("Mossbloom");
        NAMES.add("Sunpetal");
        NAMES.add("Frostleaf");
        NAMES.add("Shadowvine");
        NAMES.add("Windbranch");
    }

    public static String getRandomName() {
        int index = RANDOM.nextInt(NAMES.size());
        return NAMES.get(index);
    }
}