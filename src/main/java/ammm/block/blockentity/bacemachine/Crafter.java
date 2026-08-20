package ammm.block.blockentity.bacemachine;

import ammm.AMMMConstants;
import astral_mekanism.recipes.cachedRecipe.AstralCraftingCachedRecipe;
import astral_mekanism.recipes.inputRecipeCache.AstralCraftingRecipeCache;
import astral_mekanism.recipes.lookup.AstralCraftingRecipeLookUpHandler;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class Crafter extends TileEntityRecipeMachine<AstralCraftingRecipe>
        implements AstralCraftingRecipeLookUpHandler {

    public static final RecipeError[] NOT_ENOUGH_ITEMS = new RecipeError[] {
            RecipeError.create(), RecipeError.create(), RecipeError.create(), RecipeError.create(),
            RecipeError.create(),
            RecipeError.create(), RecipeError.create(), RecipeError.create(), RecipeError.create(),
            RecipeError.create(),
            RecipeError.create(), RecipeError.create(), RecipeError.create(), RecipeError.create(),
            RecipeError.create(),
            RecipeError.create(), RecipeError.create(), RecipeError.create(), RecipeError.create(),
            RecipeError.create(),
            RecipeError.create(), RecipeError.create(), RecipeError.create(), RecipeError.create(), RecipeError.create()
    };
    public static final RecipeError NOT_ENOUGH_FLUID = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_GAS = RecipeError.create();

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            NOT_ENOUGH_ITEMS[0],
            NOT_ENOUGH_ITEMS[1],
            NOT_ENOUGH_ITEMS[2],
            NOT_ENOUGH_ITEMS[3],
            NOT_ENOUGH_ITEMS[4],
            NOT_ENOUGH_ITEMS[5],
            NOT_ENOUGH_ITEMS[6],
            NOT_ENOUGH_ITEMS[7],
            NOT_ENOUGH_ITEMS[8],
            NOT_ENOUGH_ITEMS[9],
            NOT_ENOUGH_ITEMS[10],
            NOT_ENOUGH_ITEMS[11],
            NOT_ENOUGH_ITEMS[12],
            NOT_ENOUGH_ITEMS[13],
            NOT_ENOUGH_ITEMS[14],
            NOT_ENOUGH_ITEMS[15],
            NOT_ENOUGH_ITEMS[16],
            NOT_ENOUGH_ITEMS[17],
            NOT_ENOUGH_ITEMS[18],
            NOT_ENOUGH_ITEMS[19],
            NOT_ENOUGH_ITEMS[20],
            NOT_ENOUGH_ITEMS[21],
            NOT_ENOUGH_ITEMS[22],
            NOT_ENOUGH_ITEMS[23],
            NOT_ENOUGH_ITEMS[24],
            NOT_ENOUGH_FLUID,
            NOT_ENOUGH_GAS,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    public InputInventorySlot[] inputSlots;
    public BasicFluidTank fluidTank;
    public IGasTank gasTank;
    private OutputInventorySlot outputSlot;
    private MachineEnergyContainer<ammm.block.blockentity.bacemachine.Crafter> energyContainer;
    private FluidInventorySlot fluidSlot;
    private GasInventorySlot gasSlot;
    private EnergyInventorySlot energySlot;
    private final IInputHandler<ItemStack>[] itemInputHandlers;
    private final IInputHandler<FluidStack> fluidInputHandler;
    private final IInputHandler<GasStack> gasInputHandler;
    private final IOutputHandler<ItemStack> outputHandler;

    @SuppressWarnings("unchecked")
    public Crafter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.ITEM,
                TransmissionType.FLUID, TransmissionType.GAS);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        configComponent.setupItemIOConfig(List.<IInventorySlot>of(inputSlots), List.<IInventorySlot>of(outputSlot),
                energySlot, false);
        configComponent.setupInputConfig(TransmissionType.FLUID, fluidTank);
        configComponent.setupInputConfig(TransmissionType.GAS, gasTank);
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
        this.itemInputHandlers = new IInputHandler[25];
        for (int i = 0; i < 25; i++) {
            this.itemInputHandlers[i] = InputHelper.getInputHandler(inputSlots[i], NOT_ENOUGH_ITEMS[i]);
        }
        this.fluidInputHandler = InputHelper.getInputHandler(fluidTank, NOT_ENOUGH_FLUID);
        this.gasInputHandler = InputHelper.getInputHandler(gasTank, NOT_ENOUGH_GAS);
        this.outputHandler = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
                                                       IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addSlot(fluidSlot = FluidInventorySlot.fill(fluidTank, recipeCacheListener, 8, 90))
                .setSlotOverlay(SlotOverlay.MINUS);
        builder.addSlot(gasSlot = GasInventorySlot.fill(gasTank, recipeCacheListener, 26, 90))
                .setSlotOverlay(SlotOverlay.MINUS);
        builder.addSlot(energySlot = EnergyInventorySlot.fill(energyContainer, recipeCacheListener, 170, 18));
        inputSlots = new InputInventorySlot[25];
        for (int i : AMMMConstants.ZERO_24) {
            builder.addSlot(inputSlots[i] = InputInventorySlot.at(
                            stack -> containsInputItemOther(stack, i,
                                    Arrays.stream(inputSlots).map(IInventorySlot::getStack).toArray(ItemStack[]::new),
                                    fluidTank.getFluid(), gasTank.getStack()),
                            stack -> containsInputItem(stack, i),
                            recipeCacheListener,
                            44 + (i % 5) * 18, 18 + (i / 5) * 18))
                    .tracksWarnings(
                            slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(NOT_ENOUGH_ITEMS[i])));
        }
        builder.addSlot(outputSlot = OutputInventorySlot.at(recipeCacheListener, 170, 54))
                .tracksWarnings(slot -> slot.warning(
                        WarningType.NO_SPACE_IN_OUTPUT, getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
                                                                IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection,
                this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        fluidSlot.fillTank();
        gasSlot.fillTankOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @NotNull CachedRecipe<AstralCraftingRecipe> createNewCachedRecipe(@NotNull AstralCraftingRecipe recipe,
                                                                             int cacheIndex) {
        return new AstralCraftingCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandlers, fluidInputHandler,
                gasInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(this::getBaselineMaxOperations);
    }

    protected int getBaselineMaxOperations() {return 1;}

    @Override
    public @Nullable AstralCraftingRecipe getRecipe(int arg0) {
        return this.findFirstRecipe(itemInputHandlers, fluidInputHandler, gasInputHandler);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<AstralCraftingRecipe, AstralCraftingRecipeCache> getRecipeType() {
        return AMERecipeTypes.ASTRAL_CRAFTING;
    }

    public MachineEnergyContainer<ammm.block.blockentity.bacemachine.Crafter> getEnergyContainer() {
        return energyContainer;
    }

    public BasicFluidTank getFluidTank() {
        return fluidTank;
    }

    public IGasTank getGasTank() {
        return gasTank;
    }

    public FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }
    public double getScaledProgress() {
        return getActive() ? 1 : 0;
    }
}