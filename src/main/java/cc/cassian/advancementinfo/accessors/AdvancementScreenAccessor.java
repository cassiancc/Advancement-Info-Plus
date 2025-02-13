/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.cassian.advancementinfo.accessors;

import net.minecraft.advancement.Advancement;
//? if >1.21
/*import net.minecraft.advancement.PlacedAdvancement;*/
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.network.ClientAdvancementManager;

/**
 *
 * @author gbl
 */
public interface AdvancementScreenAccessor {
        ClientAdvancementManager advancement_info_plus$getAdvancementHandler();
        AdvancementTab advancement_info_plus$myGetTab(
                //? if >1.21 {
                /*PlacedAdvancement advancement
                 *///?} else
                Advancement advancement
        );
}
