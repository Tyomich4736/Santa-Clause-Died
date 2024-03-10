package by.nosevich.santaclausedied.game;

import android.content.Context;
import by.nosevich.santaclausedied.easteregg.impl.SendingYourDataEasterEgg;

import java.util.Collections;
import java.util.List;

public class GameState {
    private final List<Phrase> phrases;
    private final List<String> emotions;
    private int emotionCounter = 0;
    private final Context context;


    public GameState(Context context, List<Phrase> phrases, List<String> emotions) {
        this.context = context;
        this.phrases = phrases;
        this.emotions = emotions;
        Collections.shuffle(phrases);
        Collections.shuffle(emotions);
    }

    public void updatePhraseAndEmotion() {
        Collections.shuffle(phrases);
        Collections.shuffle(emotions);
        emotionCounter = 0;
    }

    public void updateEmotion() {
        if (emotionCounter != emotions.size()) {
            emotionCounter++;
        } else {
            Collections.shuffle(emotions);
            SendingYourDataEasterEgg.getInstance().activate(context);
            emotionCounter = 0;
        }
    }

    public Phrase getCurrentPhrase() {
        return phrases.iterator().next();
    }

    public String getCurrentEmotion() {
        return emotions.get(emotionCounter);
    }

    public int getCurrentEmotionNumber() {
        return emotionCounter + 1;
    }
}
