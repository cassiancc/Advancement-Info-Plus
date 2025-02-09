package cc.cassian.advancementinfo;

import cc.cassian.advancementinfo.accessors.AdvancementProgressAccessor;
import cc.cassian.advancementinfo.accessors.AdvancementScreenAccessor;
import cc.cassian.advancementinfo.accessors.AdvancementWidgetAccessor;
import cc.cassian.advancementinfo.helpers.ModHelpers;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonPrimitive;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class AdvancementInfo
{
    public static final String MOD_ID = "advancementinfo";
    public static final Logger LOGGER = LogManager.getLogger();

    static public AdvancementWidget mouseOver, mouseClicked;
    static public List<AdvancementStep> cachedClickList;
    static public int cachedClickListLineCount;
    public static boolean showAll;
    public static ModConfig config;

    public static List<AdvancementStep> getSteps(AdvancementWidgetAccessor widget) {
        List<AdvancementStep> result = new ArrayList<>();
        addStep(result, widget.getProgress(), widget.getProgress().getUnobtainedCriteria(), false);
        addStep(result, widget.getProgress(), widget.getProgress().getObtainedCriteria(), true);
        return result;        
    }
    
    private static void addStep(List<AdvancementStep> result, AdvancementProgress progress, Iterable<String> criteria, boolean obtained) {
        final String[] prefixes = new String[] { "item.minecraft", "block.minecraft", "entity.minecraft", "container", "effect.minecraft", "biome.minecraft" };
        // criteria is actually a List<> .. but play nice
        ArrayList<String> sorted = new ArrayList<>();
        for (String s:criteria) {
            sorted.add(s);
        }
        ArrayList<String> details = null;
        Collections.sort(sorted);
        for (String s: sorted) {
            String translation = null;
            String key = s;
            if (key.startsWith("minecraft:")) {
                key=key.substring(10);
            }
            if (key.startsWith("textures/entity/")) {
                String entityAppearance = key.substring(16);
                int dotPos;
                if ((dotPos = entityAppearance.indexOf("."))>0) {
                    entityAppearance = entityAppearance.substring(0, dotPos);
                }
                translation =  entityAppearance;
            }
            if (translation == null) {
                for (String prefix: prefixes) {
                    if (I18n.hasTranslation(prefix+"."+key)) {
                        translation = I18n.translate(prefix+"."+key);
                        break;
                    }
                }
            }
            if (translation == null) {
                CriterionConditions conditions = ((AdvancementProgressAccessor)(progress)).getCriterion(s).getConditions();
                if (conditions != null) {
                    JsonObject o = conditions.toJson(AdvancementEntityPredicateSerializer.INSTANCE);
                    details = new ArrayList<>();
                    JsonElement maybeEffects = o.get("effects");
                    if (maybeEffects instanceof JsonObject effects) {
                        for (Map.Entry<String, JsonElement> entry: effects.entrySet()) {
                            details.add(I18n.translate("effect."+entry.getKey().replace(':', '.')));
                        }
                    }
                    JsonElement maybeBlock = o.get("block");
                    if (maybeBlock instanceof JsonPrimitive && maybeBlock.getAsJsonPrimitive().isString()) {
                        details.add(ModHelpers.fallback(maybeBlock));
                    }

                    JsonElement maybeItems = o.get("items");
                    if (maybeItems instanceof JsonArray items) {
                        JsonObject deeperItems = items.get(0).getAsJsonObject();
                        if (deeperItems.get("items") instanceof JsonArray display) {
                            for (JsonElement entry: display) {
                                key = ModHelpers.toKey(entry);
                                final var item = "item."+key;
                                final var block = "block."+key;
                                if (I18n.hasTranslation(item)) {
                                    details.add(I18n.translate(item));
                                }
                                else if (I18n.hasTranslation(block)) {
                                    details.add(I18n.translate(block));
                                }
                            }
                            translation = ModHelpers.fallback(display.get(0));
                        }
                        if (deeperItems.get("tag") instanceof JsonArray display) {
                            for (JsonElement entry: display) {
                                key = ModHelpers.toKey(entry);
                                final var tag = "tag."+key;
                                details.add(I18n.translate(tag));
                            }
                            translation = ModHelpers.fallback(display.get(0));
                        }
                        else if (deeperItems.get("tag") instanceof JsonPrimitive) {
                            var tag = deeperItems.get("tag");
                            details.add(ModHelpers.fallback(tag, "tag.item."));
                        }
                    }

                    JsonElement maybeItem = o.get("item");
                    if (maybeItem instanceof JsonObject items) {
                        if (items.get("items") instanceof JsonArray display) {
                            for (JsonElement entry: display) {
                                key = ModHelpers.toKey(entry);
                                final var item = "item."+key;
                                final var block = "block."+key;
                                if (I18n.hasTranslation(item)) {
                                    details.add(I18n.translate(item));
                                }
                                else if (I18n.hasTranslation(block)) {
                                    details.add(I18n.translate(block));
                                }
                            }
                            translation = ModHelpers.fallback(display.get(0));
                        }
                    }

                    JsonElement maybeLocation = o.get("location");
                    if (maybeLocation instanceof JsonObject location) {
                        if (location.get("block") instanceof JsonObject object) {
                            JsonElement tag = object.get("tag");

                            if (tag instanceof JsonArray display) {
                                for (JsonElement entry: display) {
                                    key = ModHelpers.toKey(entry);
                                    details.add(I18n.translate("tag."+key));
                                }
                                translation = ModHelpers.fallback(display.get(0));
                            }
                            else if (tag instanceof JsonPrimitive) {
                                details.add(ModHelpers.fallback(tag, "tag.item."));
                            }
                        }
                    }


                    if (details.isEmpty()) {
                        System.out.println(o);
                    }
                }
                if (translation == null)
                    translation = ModHelpers.formatAsTitleCase(key);
            }
            result.add(new AdvancementStep(translation, obtained, details));
        }
    }

    public static void setMatchingFrom(AdvancementsScreen screen, String text) {
        List<AdvancementStep> result = new ArrayList<>();
        ClientAdvancementManager advancementHandler = ((AdvancementScreenAccessor)screen).advancement_info_plus$getAdvancementHandler();
        Collection<Advancement> all = advancementHandler.getManager().getAdvancements();
        int lineCount = 0;
        
        text = text.toLowerCase();
        for (Advancement adv: all) {
            if(adv.getId().getPath().startsWith("recipes/")) {
                continue;
            }
            if (adv.getDisplay() == null) {
                LOGGER.debug("! {} Has no display", adv.getId());
                continue;
            }
            if (adv.getDisplay().getTitle() == null) {
                LOGGER.debug("! {} Has no title", adv.getId());
                continue;
            }
            if (adv.getDisplay().getDescription() == null) {
                LOGGER.debug("! {} Has no description", adv.getId());
                continue;
            }
            String title = adv.getDisplay().getTitle().getString();
            String desc  = adv.getDisplay().getDescription().getString();
            LOGGER.debug("- {} {}: {} ", adv.getId(), title, desc);
            if (title.toLowerCase().contains(text)
            ||  desc.toLowerCase().contains(text)) {
                ArrayList<String> details = new ArrayList<>();
                details.add(desc);
                AdvancementTab tab = ((AdvancementScreenAccessor)screen).advancement_info_plus$myGetTab(adv);
                if (tab == null) {
                    LOGGER.info("no tab found for advancement {} title {} description {}", adv.getId(), title, desc);
                    continue;
                }
                details.add(tab.getTitle().getString());
                boolean done = ((AdvancementWidgetAccessor)(screen.getAdvancementWidget(adv))).getProgress().isDone();
                result.add(new AdvancementStep(title, done, details));
                lineCount+=3;
            }
        }
        cachedClickList = result;
        cachedClickListLineCount = lineCount;
        mouseOver = null;
    }

    public static void init() {
        Configurator.setLevel(LOGGER.getName(), Level.ALL);
        showAll = false;
        if (ModHelpers.clothConfigInstalled()) {
            ConfigHolder<ModConfig> configHolder = AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
            configHolder.registerSaveListener((holder, modConfig) -> {
                modConfig.validate();
                return ActionResult.PASS;
            });
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        } else {
            config = new ModConfig();
        }

        LOGGER.info("AdvancementInfo initialized");
    }
}
