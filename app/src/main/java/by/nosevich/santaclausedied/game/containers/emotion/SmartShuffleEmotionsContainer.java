package by.nosevich.santaclausedied.game.containers.emotion;

import by.nosevich.santaclausedied.game.Emotion;

import java.util.*;

public class SmartShuffleEmotionsContainer implements EmotionsContainer {

    private static final int EMOTIONS_MEMORY_CAPACITY = 2;

    private final List<Emotion> emotions;
    private final List<String> lastEmotionTypes = new ArrayList<>(EMOTIONS_MEMORY_CAPACITY);
    private int currentIndex = 0;

    public SmartShuffleEmotionsContainer(List<Emotion> emotions) {
        this.emotions = new ArrayList<>(emotions);
        Collections.shuffle(this.emotions);
        lastEmotionTypes.add(emotions.get(0).getType());
    }

    @Override
    public void reset() {
        Collections.shuffle(emotions);
        currentIndex = 0;
        lastEmotionTypes.clear();
        lastEmotionTypes.add(emotions.get(0).getType());
    }

    @Override
    public Emotion nextEmotion() {
        currentIndex++;
        if (currentIndex >= emotions.size()) {
            return null;
        }
        for (int searchIndex = currentIndex;
             searchIndex < emotions.size();
             searchIndex++) {
            Emotion currentEmotion = emotions.get(searchIndex);
            if (!lastEmotionTypes.contains(currentEmotion.getType())) {
                emotions.remove(searchIndex);
                emotions.add(currentIndex, currentEmotion);
                break;
            }
        }

        Emotion nextEmotion = emotions.get(currentIndex);
        if (lastEmotionTypes.size() == EMOTIONS_MEMORY_CAPACITY) {
            lastEmotionTypes.remove(lastEmotionTypes.iterator().next());
        }
        lastEmotionTypes.add(nextEmotion.getType());
        return nextEmotion;
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
