package by.nosevich.santaclausedied.dto;

public enum Setting {
    HIDE_DIALOG_PHRASES("hideDialogPhrases", "Спрятать диалоговые фразы", "Если включено, при игре в одиночный режим фразы, требующие участия более одного человека, не будут выдаваться"),
    HIDE_SWEAR_PHRASES("hideSwearPhrases", "Спрятать ругательные фразы", "Если включено, при игре в одиночный режим фразы, содержащие матерные слова, не будут выдаваться");
    private final String id;
    private final String text;
    private final String infoText;

    Setting(String id, String text, String infoText) {
        this.id = id;
        this.text = text;
        this.infoText = infoText;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getInfoText() {
        return infoText;
    }
}
