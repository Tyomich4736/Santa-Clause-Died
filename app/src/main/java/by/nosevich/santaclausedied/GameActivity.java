package by.nosevich.santaclausedied;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import by.nosevich.santaclausedied.game.*;
import by.nosevich.santaclausedied.game.containers.emotion.EmotionsContainer;
import by.nosevich.santaclausedied.game.containers.emotion.EmotionsContainerFactory;
import by.nosevich.santaclausedied.service.SettingsService;
import by.nosevich.santaclausedied.util.EmotionsParsingUtil;
import by.nosevich.santaclausedied.util.PhrasesParsingUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameActivity extends AppCompatActivity {

    private SettingsService settingsService;
    private EmotionsContainerFactory emotionsContainerFactory;

    private GameState gameState;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);
        settingsService = new SettingsService(this);
        emotionsContainerFactory = new EmotionsContainerFactory(settingsService);

        TextView phraseTextView = findViewById(R.id.phraseText);
        phraseTextView.setMovementMethod(new ScrollingMovementMethod());

        initGameState();
        updateEmotionView();
    }

    public void onNextEmotionButtonClick(View view) {
        gameState.updateEmotion();
        updateEmotionView();
    }


    public void onNextPhraseButtonClick(View view) {
        gameState.updatePhraseAndEmotion();
        TextView phraseTextView = findViewById(R.id.phraseText);
        phraseTextView.setText(gameState.getCurrentPhrase().getText());
        updateEmotionView();
    }

    private void initGameState() {
        try {
            EmotionsContainer emotionsContainer = emotionsContainerFactory.createEmotionsContainer(initEmotions());
            gameState = new GameState(this, initPhrases(), emotionsContainer);
        } catch (IOException e) {
            Toast.makeText(this, "Resources reading error", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
    }

    private void updateEmotionView() {
        TextView emotionTextView = findViewById(R.id.emotionText);
        emotionTextView.setText(gameState.getCurrentEmotion());
        TextView emotionNumberText = findViewById(R.id.emotionNumberText);
        emotionNumberText.setText(Integer.toString(gameState.getCurrentEmotionNumber()));
    }

    private List<Phrase> initPhrases() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.phrases);
        String phrasesStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        Stream<Phrase> phrasesStream = PhrasesParsingUtil.parsePhrases(phrasesStr).stream();
        if (settingsService.isSettingEnabled(Setting.HIDE_DIALOG_PHRASES)) {
            phrasesStream = phrasesStream.filter(phrase -> !phrase.getTags().contains(PhraseTag.DIALOG));
        }
        if (settingsService.isSettingEnabled(Setting.HIDE_SWEAR_PHRASES)) {
            phrasesStream = phrasesStream.filter(phrase -> !phrase.getTags().contains(PhraseTag.SWEARING));
        }
        return phrasesStream.collect(Collectors.toList());
    }

    private List<Emotion> initEmotions() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.emotions);
        String emotionsStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return EmotionsParsingUtil.parseEmotions(emotionsStr);
    }
}