package com.example.divinationservice.component;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.ModelType;

public class TokenCounter {

    private final static Encoding ENCODING = Encodings
            .newDefaultEncodingRegistry()
            .getEncodingForModel(ModelType.GPT_4O_MINI);

    public static int countTokens(String prompt) {
        return ENCODING.countTokens(prompt);
    }
}
