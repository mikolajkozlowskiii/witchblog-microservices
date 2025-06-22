package com.example.orchestratorservice.component;

import java.util.Random;

public class RandomIntegrationFailMessageGenerator {
    private static final String[] MESSAGES = {
            "The cards refuse to speak... Looks like the spirits took a coffee break. Try again!",
            "Our mystical connection to the Tarot realm has been interrupted. Probably Mercury's fault. Please try again!",
            "The future remains a mystery... Seems the Tarot hotline is down. Try once more!",
            "Oops! The cards are being shy today. No fortune-telling until the connection is back!",
            "The veil between worlds is unstable... I couldn’t fetch your Tarot reading. Let's try again in a moment!"
    };

    private static final Random RANDOM = new Random();

    public static String getRandomFortuneFail() {
        int index = RANDOM.nextInt(MESSAGES.length);
        return MESSAGES[index];
    }
}
