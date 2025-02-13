package cc.cassian.advancementinfo.neoforge;

import cc.cassian.advancementinfo.AdvancementInfo;
import cc.cassian.advancementinfo.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;


@Mod(AdvancementInfo.MOD_ID)
public final class AdvancementInfoNeoForge {
    public AdvancementInfoNeoForge() {
        // Run our common setup.
        AdvancementInfo.init();
        registerModsPage();


    }

    //Integrate Cloth Config screen (if mod present) with Forge mod menu.
    public void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer, arg) -> AutoConfig.getConfigScreen(ModConfig.class, arg).get());
    }


}
