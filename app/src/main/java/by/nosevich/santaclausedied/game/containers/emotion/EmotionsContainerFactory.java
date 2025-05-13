package by.nosevich.santaclausedied.game.containers.emotion;

import by.nosevich.santaclausedied.game.Emotion;
import by.nosevich.santaclausedied.game.Setting;
import by.nosevich.santaclausedied.service.SettingsService;

import java.util.List;

public class EmotionsContainerFactory {
    private final SettingsService settingsService;

    public EmotionsContainerFactory(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public EmotionsContainer createEmotionsContainer(List<Emotion> emotions) {
        if (settingsService.isSettingEnabled(Setting.ENABLE_SMART_EMOTIONS_SHUFFLE)) {
            return new SmartShuffleEmotionsContainer(emotions);
        }
        return new RandomBasedEmotionsContainer(emotions);
    }
}
