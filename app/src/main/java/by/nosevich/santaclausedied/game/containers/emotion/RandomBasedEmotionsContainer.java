package by.nosevich.santaclausedied.game.containers.emotion;

import by.nosevich.santaclausedied.game.Emotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomBasedEmotionsContainer implements EmotionsContainer {
    private final List<Emotion> emotions;
    private int currentIndex = 0;

    public RandomBasedEmotionsContainer(List<Emotion> emotions) {
        this.emotions = new ArrayList<>(emotions);
        Collections.shuffle(this.emotions);
    }

    @Override
    public void reset() {
        currentIndex = 0;
        Collections.shuffle(emotions);
    }

    @Override
    public Emotion nextEmotion() {
        currentIndex++;
        return currentEmotion();
    }

    @Override
    public Emotion currentEmotion() {
        if (currentIndex < emotions.size()) {
            return emotions.get(currentIndex);
        }
        return null;
    }

    @Override
    public int currentIndex() {
        return currentIndex;
    }
}
