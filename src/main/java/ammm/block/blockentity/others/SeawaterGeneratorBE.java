package ammm.block.blockentity.others;

import com.github.misosoupTgit.mwgr.block.FluidGeneratorBlockEntity;
import com.mojang.datafixers.types.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SeawaterGeneratorBE {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
    public static final RegistryObject<BlockEntityType<FluidGeneratorBlockEntity>>  SEA_WATER_GENERATOR_BE;

    private static RegistryObject<BlockEntityType<FluidGeneratorBlockEntity>> registerGeneratorBE(String name, Supplier<RegistryObject<Block>> blockReg, Supplier<Fluid> fluidSupplier) {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of((pos, state) -> {
            BlockEntityType<?> type = (BlockEntityType)ForgeRegistries.BLOCK_ENTITY_TYPES.getValue(ResourceLocation.fromNamespaceAndPath("mwgr", name));
            return new FluidGeneratorBlockEntity(type, pos, state, fluidSupplier);
        }, new Block[]{(Block)((RegistryObject)blockReg.get()).get()}).build((Type)null));
    }

    static {
        BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "mwgr");
        SEA_WATER_GENERATOR_BE = registerGeneratorBE("seawater_generator", () -> SeawaterGeneratorB.SEA_WATER_GENERATOR, () ->  ForgeRegistries.FLUIDS.getValue(ResourceLocation.fromNamespaceAndPath("mekanismelements", "seawater")));
    }
}
