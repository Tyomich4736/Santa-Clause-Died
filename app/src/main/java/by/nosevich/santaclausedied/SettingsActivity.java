package by.nosevich.santaclausedied;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import by.nosevich.santaclausedied.dto.Setting;
import by.nosevich.santaclausedied.service.SettingsService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private SettingsService settingsService;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        settingsService = new SettingsService(getApplicationContext());

        LinearLayout checkboxListLayout = findViewById(R.id.settingsList);
        List<Setting> settings = settingsService.getAllSettings();
        settings.forEach(setting -> {
            checkboxListLayout.addView(generateLayoutForSetting(setting));
        });
    }

    public void onBackButtonClick(View view) {
        finish();
    }

    @NotNull
    private LinearLayout generateLayoutForSetting(Setting setting) {
        LinearLayout checkboxLayout = new LinearLayout(this);
        checkboxLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        CheckBox checkBox = generateCheckboxForSetting(setting);
        ImageButton hintButton = generateHintButtonForSetting(setting);
        checkboxLayout.addView(checkBox);
        checkboxLayout.addView(hintButton);
        checkboxLayout.setGravity(Gravity.CENTER_VERTICAL);
        return checkboxLayout;
    }

    @NotNull
    private ImageButton generateHintButtonForSetting(Setting setting) {
        ImageButton hintButton = new ImageButton(this);
        hintButton.setImageResource(R.drawable.baseline_info_32);
        hintButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                0
        ));
        hintButton.setBackground(null);
        hintButton.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this)
                    .setMessage(setting.getInfoText())
                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
            alertDialog.show();
        });
        return hintButton;
    }

    @NotNull
    private CheckBox generateCheckboxForSetting(Setting setting) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(setting.getText());
        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsService.setSettingEnabled(setting, isChecked);
        });
        checkBox.setChecked(settingsService.isSettingEnabled(setting));
        return checkBox;
    }
}
