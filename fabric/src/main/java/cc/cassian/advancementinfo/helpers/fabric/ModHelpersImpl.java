package cc.cassian.advancementinfo.helpers.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ModHelpersImpl {
    public static boolean clothConfigInstalled() {
        return FabricLoader.getInstance().isModLoaded("cloth-config");
    }
}
