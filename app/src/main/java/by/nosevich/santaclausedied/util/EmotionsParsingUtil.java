package by.nosevich.santaclausedied.util;

import by.nosevich.santaclausedied.game.Emotion;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class EmotionsParsingUtil {

    private EmotionsParsingUtil() {
    }

    public static List<Emotion> parseEmotions(String string) {
        return Arrays.stream(string.split("@"))
                .filter(StringUtils::isNotBlank)
                .map(emotion -> {
                    Emotion emotionDto = new Emotion();
                    emotionDto.setType(emotion.substring(0, emotion.indexOf("\r\n")).trim());
                    emotionDto.setText(emotion.substring(emotion.indexOf("\r\n") + 2).trim());
                    return emotionDto;
                }).collect(Collectors.toList());
    }
}
