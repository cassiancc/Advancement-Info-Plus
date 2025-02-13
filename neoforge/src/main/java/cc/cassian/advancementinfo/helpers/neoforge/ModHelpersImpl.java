package cc.cassian.advancementinfo.helpers.neoforge;


import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;

public class ModHelpersImpl {
    public static boolean clothConfigInstalled() {
        return ModList.get().isLoaded("cloth_config");
    }

    public static boolean isDevelopment() {
        return !FMLEnvironment.production;
    }
}
