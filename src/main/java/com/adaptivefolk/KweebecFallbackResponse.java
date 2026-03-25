package com.adaptivefolk;

import java.util.List;
import java.util.Random;

public class KweebecFallbackResponse {
    private static final List<String> FALLBACKS = List.of(
            "You look very nice.",
            "Do you grow into a tree too?",
            "What is it like to not be Kweebec?",
            "Have you smelled the sunlight today?",
            "Do you dream of leaves?",
            "I once saw a flower that sneezed!",
            "The wind told me a secret...",
            "Do you think moss can gossip?",
            "Why do humans wear so many colors?",
            "I like the sound of dripping water.",
            "Does your hair taste like rain?",
            "Have you danced with a butterfly?",
            "The sun tickles my branches.",
            "Why do clouds move so slowly?",
            "I wonder if stones dream at night.",
            "Would you like a petal for a hat?",
            "Do stars ever fall into rivers?",
            "I hear the soil singing sometimes.",
            "Have you ever hugged a rock?",
            "I once tried to talk to a mushroom… it ignored me.",
            "Have you felt the tickle of morning dew?",
            "Do roots ever get itchy?",
            "I wonder if shadows like to dance.",
            "The river hums lullabies at night.",
            "Do you think the stars taste like honey?",
            "I sometimes hum to the flowers.",
            "Have you ever seen a cloud nap?",
            "Do trees get lonely without friends?",
            "I like to count the petals on new blossoms.",
            "Do you think sunlight tells stories?",
            "I once raced a squirrel, but it won.",
            "Have you smelled the first rain of spring?",
            "The wind always shares little secrets.",
            "Do stones remember old songs?",
            "I wonder what dreams taste like.",
            "Do spiders tell riddles at night?",
            "Have you ever seen moss grow upside down?",
            "I like watching shadows chase each other."
    );

    private static final Random RANDOM = new Random();

    public static String getResponse() {
        return FALLBACKS.get(RANDOM.nextInt(FALLBACKS.size()));
    }
}