package cc.cassian.advancementinfo.client.fabric;

import cc.cassian.advancementinfo.AdvancementInfo;
import net.fabricmc.api.ClientModInitializer;

public final class AdvancementInfoFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        AdvancementInfo.init();
    }

}
