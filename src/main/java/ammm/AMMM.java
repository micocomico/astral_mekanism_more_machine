package ammm;

import ammm.block.blockentity.astralfactory.AFCombining;
import ammm.block.blockentity.astralfactory.AFCompressing;
import ammm.block.blockentity.astralfactory.AFCrushing;
import ammm.block.blockentity.astralfactory.AFEnriching;
import ammm.block.blockentity.astralmachine.AMCrafter;
import ammm.block.blockentity.enchantedfactory.EFCombining;
import ammm.block.blockentity.enchantedfactory.EFCrushing;
import ammm.block.blockentity.enchantedfactory.EFEnergizedSmelting;
import ammm.block.blockentity.enchantedfactory.EFEnriching;
import ammm.block.blockentity.enchantedmachine.EMCombiner;
import ammm.block.blockentity.enchantedmachine.EMCrafter;
import ammm.block.blockentity.normalfactory.NFCombining;
import ammm.block.blockentity.normalfactory.NFCrushing;
import ammm.block.blockentity.normalfactory.NFEnriching;
import ammm.block.gui.factory.GFAdvanced;
import ammm.block.gui.factory.GFCombining;
import ammm.block.gui.factory.GFElectric;
import ammm.block.gui.machine.GuiCrafter;
import ammm.registration.MachineRegistryObject;
import ammm.registries.AMMMCreativeTab;
import ammm.registries.AMMMachines;
import astral_mekanism.block.gui.factory.GuiEnergizedSmeltingFactory;
import astral_mekanism.block.gui.prefab.GuiDoubleItemToItemRecipeMachine;
import com.mojang.logging.LogUtils;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AMMM.MODID)
public class AMMM {

    // Define mod id in a common place for everything to reference
    public static final String MODID = AMMMConstants.MODID;
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "astral_mekanism_more_machine" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "astral_mekanism_more_machine" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "astral_mekanism_more_machine" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public AMMM() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        AMMMCreativeTab.CREATIVE_TABS.register(modEventBus);
        AMMMachines.MACHINES.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);


        ammm.Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
        initScreens();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    private static void initScreens() {
        registerScreenMek(AMMMachines.ASTRAL_CRAFTER, GuiCrafter<AMCrafter>::new);
        registerScreenMek(AMMMachines.ENCHANTED_CRAFTER, GuiCrafter<EMCrafter>::new);

        AMMMachines.ENCHANTED_ENERGIZED_SMELTING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GuiEnergizedSmeltingFactory<EFEnergizedSmelting>::new));

        AMMMachines.ASTRAL_CRUSHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<AFCrushing>::new));
        AMMMachines.ENCHANTED_CRUSHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<EFCrushing>::new));
        AMMMachines.CRUSHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<NFCrushing>::new));

        AMMMachines.ASTRAL_ENRICHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<AFEnriching>::new));
        AMMMachines.ENCHANTED_ENRICHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<EFEnriching>::new));
        AMMMachines.ENRICHING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFElectric<NFEnriching>::new));

        registerScreenMek(AMMMachines.ENCHANTED_COMBINER, GuiDoubleItemToItemRecipeMachine<EMCombiner>::new);
        AMMMachines.ASTRAL_COMBINING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFCombining<AFCombining>::new));
        AMMMachines.ENCHANTED_COMBINING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFCombining<EFCombining>::new));
        AMMMachines.COMBINING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFCombining<NFCombining>::new));

        AMMMachines.ASTRAL_COMPRESSING_FACTORIES.forEach((t, object) -> registerScreenMek(object, GFAdvanced<AFCompressing>::new));
    }

    private static <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, U extends Screen & MenuAccess<CONTAINER>> void registerScreenMek(
            MachineRegistryObject<BE, ?, ? extends CONTAINER, ?> registryObject,
            ScreenConstructor<CONTAINER, U> constructor) {
        MenuScreens.register(registryObject.getContainer().get(), constructor);
    }
}