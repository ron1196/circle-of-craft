package io.github.ron1196.thelionking.quest.animal;

import java.util.Random;

/**
 * Static utility class for animal mini-quest phrases and rewards.
 * Per-player quest state is now stored in {@link io.github.ron1196.thelionking.entity.animal.LKAnimal}.
 */
public final class LKAnimalQuest {

    private static final Random RANDOM = new Random();

    static final String[] QUEST_START_PHRASES = {
            "I'm getting quite hungry. If you can bring me # %, there might be a reward for you!",
            "Bring me # % to eat and I'll give you something useful!",
            "I could really do with # %. Can you help me out?",
            "I've been looking for # %. Bring some and I'll make it worth your while!",
            "If you bring me # %, I'll have a reward ready for you.",
            "I fancy # % right about now. Could you fetch some?"
    };

    static final String[] QUEST_END_PHRASES = {
            "Delicious! Here, take this as a reward.",
            "Thank you so much! Here's something for your trouble.",
            "That really hit the spot! Take this reward.",
            "Perfect! You've earned this."
    };

    static final String[] NUMBER_WORDS = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
            "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
            "seventeen", "eighteen", "nineteen", "twenty", "twenty-one", "twenty-two",
            "twenty-three", "twenty-four", "twenty-five", "twenty-six", "twenty-seven",
            "twenty-eight", "twenty-nine", "thirty", "thirty-one", "thirty-two",
            "thirty-three", "thirty-four", "thirty-five", "thirty-six", "thirty-seven",
            "thirty-eight", "thirty-nine", "forty", "forty-one", "forty-two", "forty-three",
            "forty-four", "forty-five", "forty-six", "forty-seven", "forty-eight",
            "forty-nine", "fifty", "fifty-one", "fifty-two", "fifty-three", "fifty-four",
            "fifty-five", "fifty-six", "fifty-seven", "fifty-eight", "fifty-nine",
            "sixty", "sixty-one", "sixty-two", "sixty-three", "sixty-four"
    };

    private LKAnimalQuest() {
    }

    public static String getQuestStartMessage(String animalName, String itemName, int amount) {
        String phrase = QUEST_START_PHRASES[RANDOM.nextInt(QUEST_START_PHRASES.length)];
        phrase = phrase.replace("#", numberToWord(amount)).replace("%", itemName);
        return "\u00a7e<" + animalName + "> \u00a7f" + phrase;
    }

    private static String numberToWord(int number) {
        if (number >= 0 && number < NUMBER_WORDS.length) {
            return NUMBER_WORDS[number];
        }
        return String.valueOf(number);
    }

    public static String getQuestEndMessage(String animalName) {
        String phrase = QUEST_END_PHRASES[RANDOM.nextInt(QUEST_END_PHRASES.length)];
        return "\u00a7e<" + animalName + "> \u00a7f" + phrase;
    }
}
