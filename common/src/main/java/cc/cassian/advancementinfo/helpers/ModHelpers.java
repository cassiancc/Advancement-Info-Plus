package cc.cassian.advancementinfo.helpers;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ModHelpers {

    public static String formatAsTitleCase(String text) {
        text = text.replaceAll("[_./]", " ");
        StringBuilder newText = new StringBuilder();
        int i = 0;
        for (Character c : text.toCharArray()) {
            if (i == 0) {
                c = c.toString().toUpperCase().charAt(0);
            }
            newText.append(c);
            i++;
            if (c == ' ') {
                i = 0;
            }
        }
        return newText.toString();
    }

    @ExpectPlatform
    public static boolean clothConfigInstalled() {
        throw new AssertionError();
    }
}
