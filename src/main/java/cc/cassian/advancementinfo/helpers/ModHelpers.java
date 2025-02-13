package cc.cassian.advancementinfo.helpers;

import com.google.gson.JsonElement;
import com.ibm.icu.impl.Assert;
import dev.architectury.injectables.annotations.ExpectPlatform;
//? if >1.21
/*import net.minecraft.client.gui.DrawContext;*/
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (entry != null)
            return entry.toString().replace(':', '.').replace("/",".").replace("\"", "");
        return "";
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
        if (entry != null) {
            String key = toKey(entry);
            if (I18n.hasTranslation(prefix+key))
                return I18n.translate(prefix+key);
            else
                return formatAsTitleCase(entry.toString());
        }
        return "";
    }

    public static ArrayList<String> createMultilineTranslation(String loreKey) {
        //Setup list to store multi-line tooltip.
        ArrayList<String> lines = new ArrayList<>();
        int maxLength = 20;
        //Check if the key exists.
        if (!loreKey.isBlank()) {
            //Translate the lore key.
            String translatedKey = I18n.translate(loreKey);
            //Check if the translated key exists.
            //Check if custom wrapping should be used.
            //Any tooltip longer than XX characters should be shortened.
            while (translatedKey.length() >= maxLength) {
                //Find how much to shorten the tooltip by.
                int index = getIndex(translatedKey, maxLength);
                //Add a shortened tooltip.
                lines.add(translatedKey.substring(0, index));
                //Remove the shortened tooltip substring from the tooltip. Repeat.
                translatedKey = translatedKey.substring(index);
            }
            //Add the final tooltip.
            if (!translatedKey.isBlank())
                lines.add(translatedKey);
        }
        return lines;
    }

    //Handles detection of when a line break should be added in a tooltip.
    public static int getIndex(String translatedKey, int maxLength) {
        String subKey = translatedKey.substring(0, maxLength);
        int index;
        //Find the last space character in the substring, if not, default to the length of the substring.
        if (subKey.contains(" ")) {
            index = subKey.lastIndexOf(" ")+1;
        }
        else index = maxLength;
        return index;
    }

    @ExpectPlatform
    public static boolean clothConfigInstalled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isDevelopment() {
        throw new AssertionError();
    }

    //? if <1.21 {
    public static void drawTexture(MatrixStack stack, int x, int y, int u, int v, int width, int height, AdvancementsScreen that) {
        that.drawTexture(stack, x, y, u,v,width,height);
    }//?}

    //? if >1.21 {
    /*public static void drawTexture(Identifier texture, int x, int y, int u, int v, int width, int height, DrawContext that) {
        that.drawTexture(RenderLayer::getGuiTextured, texture, x, y, u, v, width, height, 256, 256);
    }*///?}



}
