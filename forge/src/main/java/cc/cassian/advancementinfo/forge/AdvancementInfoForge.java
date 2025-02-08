package cc.cassian.advancementinfo.forge;

import cc.cassian.advancementinfo.AdvancementInfo;
import cc.cassian.advancementinfo.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;


@Mod(AdvancementInfo.MOD_ID)
public final class AdvancementInfoForge {
    public AdvancementInfoForge() {
        // Run our common setup.
        AdvancementInfo.init();
        registerModsPage();


    }

    //Integrate Cloth Config screen (if mod present) with Forge mod menu.
    public void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()));
    }


}
