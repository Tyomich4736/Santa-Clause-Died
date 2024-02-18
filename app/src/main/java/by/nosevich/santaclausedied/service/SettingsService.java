package by.nosevich.santaclausedied.service;

import android.content.Context;
import android.content.SharedPreferences;
import by.nosevich.santaclausedied.dto.Setting;

import java.util.Arrays;
import java.util.List;

public class SettingsService {

    private final SharedPreferences settings;

    public SettingsService(Context applicationContext) {
        settings = applicationContext.getSharedPreferences("SantaClauseDiedSettings", 0);
    }

    public List<Setting> getAllSettings() {
        return Arrays.asList(Setting.values());
    }

    public boolean isSettingEnabled(Setting setting) {
        return settings.getBoolean(setting.getId(), false);
    }

    public void setSettingEnabled(Setting setting, boolean isEnabled) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(setting.getId(), isEnabled);
        editor.apply();
    }
}
