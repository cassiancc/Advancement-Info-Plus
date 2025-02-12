package cc.cassian.advancementinfo.helpers.forge;


import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ModHelpersImpl {
    public static boolean clothConfigInstalled() {
        return ModList.get().isLoaded("cloth_config");
    }

    public static boolean isDevelopment() {
        return !FMLEnvironment.production;
    }
}
