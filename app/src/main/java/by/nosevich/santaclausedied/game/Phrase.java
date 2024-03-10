package by.nosevich.santaclausedied.game;

import java.util.Set;

public class Phrase {
    private String text;
    private Set<PhraseTag> tags;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<PhraseTag> getTags() {
        return tags;
    }

    public void setTags(Set<PhraseTag> tags) {
        this.tags = tags;
    }
}
