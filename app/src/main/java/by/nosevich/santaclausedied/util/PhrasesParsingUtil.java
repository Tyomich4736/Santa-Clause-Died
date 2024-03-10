package by.nosevich.santaclausedied.util;

import by.nosevich.santaclausedied.game.Phrase;
import by.nosevich.santaclausedied.game.PhraseTag;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class PhrasesParsingUtil {

    private PhrasesParsingUtil() {
    }

    public static List<Phrase> parsePhrases(String string) {
        return Arrays.stream(string.split("@"))
                .filter(StringUtils::isNotBlank)
                .map(phrase -> {
                    Phrase phraseDto = new Phrase();
                    phraseDto.setTags(parseTags(phrase.substring(0, phrase.indexOf("\r\n"))));
                    phraseDto.setText(phrase.substring(phrase.indexOf("\r\n") + 2).trim());
                    return phraseDto;
                }).collect(Collectors.toList());
    }

    private static Set<PhraseTag> parseTags(String substring) {
        return Arrays.stream(substring.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .map(tagName -> PhraseTag.valueOf(tagName.toUpperCase()))
                .collect(Collectors.toSet());
    }
}
