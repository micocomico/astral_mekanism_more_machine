package ammm.block.blockentity.others;

import com.github.misosoupTgit.mwgr.block.FluidGeneratorBlock;
import com.github.misosoupTgit.mwgr.block.FluidGeneratorBlockEntity;
import com.github.misosoupTgit.mwgr.compat.MekanismCompat;
import com.github.misosoupTgit.mwgr.item.MWGRColorBlockItems;
import com.github.misosoupTgit.mwgr.item.MWGRItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SeawaterGeneratorB {
    public static final DeferredRegister<Block> BLOCKS;
    public static final RegistryObject<Block> SEA_WATER_GENERATOR;

    private static RegistryObject<Block> registerGenerator(String name, Supplier<BlockBehaviour.Properties> propertiesSupplier, Supplier<RegistryObject<BlockEntityType<FluidGeneratorBlockEntity>>> typeSupplier, Supplier<Fluid> fluidSupplier, String descriptionKey, String hexColor, boolean isBold) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> {
            BlockBehaviour.Properties props = (BlockBehaviour.Properties)propertiesSupplier.get();
            Supplier<BlockEntityType<FluidGeneratorBlockEntity>> lazyType = () -> (BlockEntityType)((RegistryObject)typeSupplier.get()).get();
            return (Block)(MekanismCompat.isMekanismLoaded() ? MekanismCompat.createMekanismBlock(props, lazyType, fluidSupplier, descriptionKey) : new FluidGeneratorBlock(props, lazyType, fluidSupplier, descriptionKey));
        });
        MWGRItems.ITEMS.register(name, () -> (Item)(MekanismCompat.isMekanismLoaded() ? MekanismCompat.createInfiniteFluidHandlerItem((Block)block.get(), new Item.Properties(), hexColor, isBold, fluidSupplier) : new MWGRColorBlockItems((Block)block.get(), new Item.Properties(), hexColor, isBold)));
        return block;
    }

    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "mwgr");
        SEA_WATER_GENERATOR = registerGenerator("seawater_generator", () -> BlockBehaviour.Properties.of().mapColor(Blocks.IRON_BLOCK.defaultMapColor()).strength(0.8F, 128.0F).sound(SoundType.STONE), () -> SeawaterGeneratorBE.SEA_WATER_GENERATOR_BE, () -> ForgeRegistries.FLUIDS.getValue(ResourceLocation.fromNamespaceAndPath("mekanismelements", "seawater")), "description.mwgr.seawater_generator", "#06C9E6", true);
    }
}
