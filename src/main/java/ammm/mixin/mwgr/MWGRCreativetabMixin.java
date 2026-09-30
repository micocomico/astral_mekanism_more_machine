package ammm.mixin.mwgr;

import ammm.block.blockentity.others.SeawaterGeneratorB;
import com.github.misosoupTgit.mwgr.MWGRMod;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MWGRMod.class,remap = false)
public abstract class MWGRCreativetabMixin {
    @Inject(method = "addCreative",at = @At("TAIL"))
    private void add(BuildCreativeModeTabContentsEvent event,CallbackInfo ci){
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept((ItemLike) SeawaterGeneratorB.SEA_WATER_GENERATOR.get());
        }
    }
}
