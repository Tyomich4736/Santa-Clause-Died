package by.nosevich.santaclausedied.util;

import by.nosevich.santaclausedied.dto.PhraseDto;
import by.nosevich.santaclausedied.dto.PhraseTag;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class PhrasesParsingUtil {

    private PhrasesParsingUtil() {
    }

    public static List<PhraseDto> parsePhrases(String string) {
        return Arrays.stream(string.split("@"))
                .filter(StringUtils::isNotBlank)
                .map(phrase -> {
                    PhraseDto phraseDto = new PhraseDto();
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
