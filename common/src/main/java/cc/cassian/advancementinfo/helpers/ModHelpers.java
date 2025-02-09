package cc.cassian.advancementinfo.helpers;

import com.google.gson.JsonElement;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resource.language.I18n;

public class ModHelpers {

    public static String formatAsTitleCase(String text) {
        if (text.contains(":")) {
            text = text.split(":")[1];
        }
        text = text.replaceAll("[_./\"]", " ");
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

    public static String toKey(JsonElement entry) {
        return entry.toString().replace(':', '.').replace("\"", "");
    }

    public static String fallback(JsonElement entry) {
        String key = toKey(entry);
        if (I18n.hasTranslation("item."+key)) {
            return I18n.translate("item."+key);
        }
        if (I18n.hasTranslation("block."+key)) {
            return I18n.translate("block."+key);
        }
        if (I18n.hasTranslation("tag."+key)) {
            return I18n.translate("tag."+key);
        }
        else {
            return formatAsTitleCase(entry.toString());
        }
    }

    public static String fallback(JsonElement entry, String prefix) {
        String key = toKey(entry);
        if (I18n.hasTranslation(prefix+key)) {
            return I18n.translate(prefix+key);
        }
        else {
            return formatAsTitleCase(entry.toString());
        }
    }

    @ExpectPlatform
    public static boolean clothConfigInstalled() {
        throw new AssertionError();
    }
}
