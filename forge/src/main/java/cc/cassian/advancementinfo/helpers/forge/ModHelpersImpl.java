package cc.cassian.advancementinfo.helpers.forge;


import net.minecraftforge.fml.ModList;

public class ModHelpersImpl {
    public static boolean clothConfigInstalled() {
        return ModList.get().isLoaded("cloth_config");
    }
}
