package by.nosevich.santaclausedied.game;

import android.content.Context;
import by.nosevich.santaclausedied.easteregg.impl.SendingYourDataEasterEgg;
import by.nosevich.santaclausedied.game.containers.emotion.EmotionsContainer;

import java.util.Collections;
import java.util.List;

public class GameState {
    private final List<Phrase> phrases;
    private final EmotionsContainer emotionsContainer;
    private final Context context;


    public GameState(Context context, List<Phrase> phrases, EmotionsContainer emotionsContainer) {
        this.context = context;
        this.phrases = phrases;
        this.emotionsContainer = emotionsContainer;
        Collections.shuffle(phrases);
    }

    public void updatePhraseAndEmotion() {
        Collections.shuffle(phrases);
        emotionsContainer.reset();
    }

    public void updateEmotion() {
        if (emotionsContainer.nextEmotion() == null) {
            emotionsContainer.reset();
            SendingYourDataEasterEgg.getInstance().activate(context);
        }
    }

    public Phrase getCurrentPhrase() {
        return phrases.iterator().next();
    }

    public String getCurrentEmotion() {
        return emotionsContainer.currentEmotion().getText();
    }

    public int getCurrentEmotionNumber() {
        return emotionsContainer.currentIndex() + 1;
    }
}
