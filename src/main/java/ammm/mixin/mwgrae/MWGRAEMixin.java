package ammm.mixin.mwgrae;

import ammm.block.blockentity.others.SeawaterGeneratorB;
import appeng.api.storage.StorageCells;
import com.github.misosoupTgit.mwgr.MWGRMod;
import com.takenokoshi.mwgrae.MwgrAE;
import com.takenokoshi.mwgrae.WaterGeneratorCellHandler;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MwgrAE.class,remap = false)
public abstract class MWGRAEMixin {
    @Inject(method = "commonSetUp",at = @At("TAIL"))
    private void add(FMLCommonSetupEvent event, CallbackInfo ci){
        if (ModList.get().isLoaded("mekanismelements")) {
            StorageCells.addCellHandler(new WaterGeneratorCellHandler((Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation("mwgr", "seawater_generator")), (Fluid)((Holder)ForgeRegistries.FLUIDS.getHolder(new ResourceLocation("mekanismelements", "seawater")).get()).get(), "seawater_generator"));
        }
    }
}
