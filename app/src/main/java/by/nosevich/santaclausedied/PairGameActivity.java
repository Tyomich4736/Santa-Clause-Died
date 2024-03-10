package by.nosevich.santaclausedied;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import by.nosevich.santaclausedied.game.GameState;
import by.nosevich.santaclausedied.game.Phrase;
import by.nosevich.santaclausedied.game.PhraseTag;
import by.nosevich.santaclausedied.game.Setting;
import by.nosevich.santaclausedied.service.SettingsService;
import by.nosevich.santaclausedied.util.PhrasesParsingUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PairGameActivity extends AppCompatActivity {

    public static final String RESOURCES_SPLITTER = "\r\n@\r\n";

    private SettingsService settingsService;

    private GameState playerOneGameState;
    private GameState playerTwoGameState;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_pair_game);
        settingsService = new SettingsService(this);

        initGameStates();
        updateBothPhrasesAndEmotionsView();
    }

    @Override
    protected void onDestroy() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        super.onDestroy();
    }

    public void onNextEmotionsButtonClick(View view) {
        onNextPlayerOneEmotionButtonClick(view);
        onNextPlayerTwoEmotionButtonClick(view);
    }

    public void onNextPlayerOneEmotionButtonClick(View view) {
        playerOneGameState.updateEmotion();
        updatePlayerOneEmotionView();
    }

    public void onNextPlayerTwoEmotionButtonClick(View view) {
        playerTwoGameState.updateEmotion();
        updatePlayerTwoEmotionView();
    }

    public void onNextPhrasesAndEmotionsButtonClick(View view) {
        playerOneGameState.updatePhraseAndEmotion();
        playerTwoGameState.updatePhraseAndEmotion();
        updateBothPhrasesAndEmotionsView();
    }

    public void onNextPlayerOnePhraseButtonClick(View view) {
        do {
            playerOneGameState.updatePhraseAndEmotion();
        } while (playerOneGameState.getCurrentPhrase().getTags().contains(PhraseTag.DIALOG));
        TextView textView = findViewById(R.id.playerOnePhraseText);
        textView.setText(playerOneGameState.getCurrentPhrase().getText());
        updatePlayerOneEmotionView();
    }

    public void onNextPlayerTwoPhraseButtonClick(View view) {
        playerTwoGameState.updatePhraseAndEmotion();
        TextView textView = findViewById(R.id.playerTwoPhraseText);
        textView.setText(playerTwoGameState.getCurrentPhrase().getText());
        updatePlayerTwoEmotionView();
    }

    private void updateBothPhrasesAndEmotionsView() {
        TextView playerOnePhraseTextView = findViewById(R.id.playerOnePhraseText);
        TextView playerTwoPhraseTextView = findViewById(R.id.playerTwoPhraseText);
        Phrase playerOnePhrase = playerOneGameState.getCurrentPhrase();
        playerOnePhraseTextView.setText(playerOnePhrase.getText());
        if (!playerOnePhrase.getTags().contains(PhraseTag.DIALOG)) {
            playerTwoPhraseTextView.setText(playerTwoGameState.getCurrentPhrase().getText());
        } else {
            playerTwoPhraseTextView.setText("(Диалог с Игроком 1)");
        }
        updatePlayerOneEmotionView();
        updatePlayerTwoEmotionView();
    }

    private void updatePlayerOneEmotionView() {
        TextView emotionText = findViewById(R.id.playerOneEmotionText);
        emotionText.setText(playerOneGameState.getCurrentEmotion());
        TextView emotionNumberText = findViewById(R.id.playerOneEmotionNumberText);
        emotionNumberText.setText(Integer.toString(playerOneGameState.getCurrentEmotionNumber()));
    }

    private void updatePlayerTwoEmotionView() {
        TextView emotionText = findViewById(R.id.playerTwoEmotionText);
        emotionText.setText(playerTwoGameState.getCurrentEmotion());
        TextView emotionNumberText = findViewById(R.id.playerTwoEmotionNumberText);
        emotionNumberText.setText(Integer.toString(playerTwoGameState.getCurrentEmotionNumber()));
    }

    private void initGameStates() {
        try {
            List<Phrase> phrases = initPhrases();
            List<String> emotions = initEmotions();
            playerOneGameState = new GameState(this, phrases, emotions);
            playerTwoGameState = new GameState(this, preparePhrasesForSecondPlayer(phrases), new ArrayList<>(emotions));
        } catch (IOException e) {
            Toast.makeText(this, "Resources reading error", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
    }

    @NotNull
    private static List<Phrase> preparePhrasesForSecondPlayer(List<Phrase> phrases) {
        return phrases.stream().filter(phrase -> !phrase.getTags().contains(PhraseTag.DIALOG)).collect(Collectors.toList());
    }

    private List<Phrase> initPhrases() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.phrases);
        String phrasesStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        Stream<Phrase> phrasesStream = PhrasesParsingUtil.parsePhrases(phrasesStr).stream();
        if (settingsService.isSettingEnabled(Setting.HIDE_SWEAR_PHRASES)) {
            phrasesStream = phrasesStream.filter(phrase -> !phrase.getTags().contains(PhraseTag.SWEARING));
        }
        return phrasesStream.collect(Collectors.toList());
    }

    private List<String> initEmotions() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.emotions);
        String[] itemsArray = IOUtils.toString(inputStream, StandardCharsets.UTF_8).split(RESOURCES_SPLITTER);
        return Arrays.stream(itemsArray).filter(StringUtils::isNoneBlank).collect(Collectors.toList());
    }
}