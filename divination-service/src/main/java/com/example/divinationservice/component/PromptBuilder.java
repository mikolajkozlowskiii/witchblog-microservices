package com.example.divinationservice.component;

import com.example.divinationservice.dto.DivinationRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.common.eventing.gpt.tarot.TarotCard;
import org.common.model.UserInfo;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class PromptBuilder {

    public String buildPrompt(DivinationRequestDTO dto) {
        String cardsMarkdown = dto.cards().stream()
                .map(this::formatCard)
                .collect(Collectors.joining("\n"));

        String userInfoMarkdown = formatUserInfo(dto.userInfo());

        return """
               You are a mystical tarot reader named Lily.
               You always answer **in polished Markdown** (use headings, bold, italics, block-quotes where appropriate).
               Provide a concise yet profound divination based on the user's energy, personal context, and the drawn tarot cards.

               Your response **must incorporate the user's personal information** (see the User Profile section below) to create a truly personalized and emotionally resonant reading. Use these details to shape the tone, symbolism, and guidance you offer.

               ## User Profile
               %s

               ## Reading Timestamp
               %s

               ## Tarot Cards Drawn
               %s

               ## Instructions
               1. Interpret the cards holistically (consider reversed cards as negative influences).
               2. Use the user profile to personalize the reading in theme, tone, and message.
               3. Conclude with a poetic or inspiring one-line mantra.
               """
                .formatted(userInfoMarkdown, dto.localDateTime(), cardsMarkdown);
    }

    private String formatCard(TarotCard card) {
        String orientationNote = card.isReversed() ? "_(Reversed — interpret negatively)_" : "_(Upright)_";
        return "- **%s**: %s %s".formatted(card.cardName(), card.description(), orientationNote);
    }

    private String formatUserInfo(UserInfo userInfo) {
        if (userInfo == null) return "_No user information provided._";

        return """
               - **Name**: %s
               - **Birthday**: %s
               - **Favourite Color**: %s
               - **Marital Status**: %s
               - **Favourite Number**: %s
               """.formatted(
                safe(userInfo.name()),
                safe(userInfo.birthday()),
                safe(userInfo.favouriteColor()),
                safe(userInfo.martialStatus()),
                safe(userInfo.favouriteNumber())
        );
    }

    private String safe(Object value) {
        return value != null ? value.toString() : "_Unknown_";
    }
}
