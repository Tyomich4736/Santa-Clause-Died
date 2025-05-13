package by.nosevich.santaclausedied.game.containers.emotion;

import by.nosevich.santaclausedied.game.Emotion;

public interface EmotionsContainer {
    void reset();
    Emotion nextEmotion();
    Emotion currentEmotion();
    int currentIndex();
}
