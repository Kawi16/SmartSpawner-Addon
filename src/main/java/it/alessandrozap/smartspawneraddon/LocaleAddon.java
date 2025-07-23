package it.alessandrozap.smartspawneraddon;

import it.alessandrozap.utilsapi.managers.messages.Locale;

public class LocaleAddon {
    public static Locale RELOADED_CONFIRM;

    public static void init() {
        RELOADED_CONFIRM = new Locale("files-reloaded");
    }
}
