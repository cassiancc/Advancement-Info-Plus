/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.cassian.advancementinfo.mixin;

import java.util.Map;
import java.util.Set;

import cc.cassian.advancementinfo.accessors.AdvancementProgressAccessor;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementProgress;
//? if >1.21
/*import net.minecraft.advancement.AdvancementRequirements;*/
import net.minecraft.advancement.criterion.CriterionProgress;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementProgress.class)
public class AdvancementProgressMixin
        //? if <1.21
        implements AdvancementProgressAccessor {

    @Unique
    private
    //? if >1.21 {
    /*Set<String>
    *///?} else
    Map<String, AdvancementCriterion>
    advancement_info_plus$savedCriteria;
    
    @Inject(method="init", at=@At("HEAD"))
    public void saveCriteria(
            //? if >1.21 {
            /*AdvancementRequirements criteria
            *///?} else
            Map<String, AdvancementCriterion> criteria, String[][] strings
            ,CallbackInfo ci) {
        advancement_info_plus$savedCriteria = criteria

        //? if >1.21 {
        /*.getNames()
        *///?}

        ;
    }
    //? if <1.21 {
    @Override
    public AdvancementCriterion advancement_info_plus$getCriteria(String name) {
        return advancement_info_plus$savedCriteria.get(name);
    }//?}
}
