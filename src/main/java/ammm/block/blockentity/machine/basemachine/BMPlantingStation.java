package ammm.block.blockentity.machine.basemachine;

import ammm.recipes.cached.FormulizedPlantingCachedRecipe;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.DoubleItemOutput;
import com.jerry.mekmm.api.ITileEntityMekanismAccessor;
import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.common.recipe.MoreMachineRecipeType;
import com.jerry.mekmm.common.util.MoreMachineUtils;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerChemicalTankWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.integration.computer.computercraft.ComputerConstants;
import mekanism.common.integration.energy.EnergyCompatUtils;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler.ItemChemicalRecipeLookupHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler.ConstantUsageRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public abstract class BMPlantingStation extends TileEntityRecipeMachine<PlantingRecipe> implements ItemChemicalRecipeLookupHandler<Gas, GasStack, PlantingRecipe>, ConstantUsageRecipeLookupHandler, IBoundingBlock {

    public static final RecipeError NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR = RecipeError.create();
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    @WrappingComputerMethod(wrapper = ComputerChemicalTankWrapper.class,
                            methodNames = { "getChemical", "getChemicalCapacity", "getChemicalNeeded", "getChemicalFilledPercentage" },
                            docPlaceholder = "chemical tank")
    public IGasTank gasTank;

    protected final IOutputHandler<DoubleItemOutput> outputHandler;
    protected final IInputHandler<@NotNull ItemStack> itemInputHandler;
    protected final ILongInputHandler<@NotNull GasStack> gasInputHandler;

    private MachineEnergyContainer<BMPlantingStation> energyContainer;

    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getInput", docPlaceholder = "input slot")
    public InputInventorySlot inputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutput", docPlaceholder = "output slot")
    OutputInventorySlot mainOutputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getSecondaryOutput", docPlaceholder = "secondary output slot")
    OutputInventorySlot secondaryOutputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getChemicalItem", docPlaceholder = "chemical input slot")
    GasInventorySlot gasSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem", docPlaceholder = "energy slot")
    EnergyInventorySlot energySlot;

    public BMPlantingStation(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.GAS, TransmissionType.ENERGY);
        configComponent.setupItemIOExtraConfig(inputSlot, mainOutputSlot, gasSlot, energySlot);
        configComponent.setupItemIOConfig(Collections.singletonList(inputSlot), List.of(mainOutputSlot, secondaryOutputSlot), energySlot, false);

        configComponent.setupIOConfig(TransmissionType.GAS, gasTank, RelativeSide.RIGHT).setCanEject(false);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);

        itemInputHandler = InputHelper.getInputHandler(inputSlot, RecipeError.NOT_ENOUGH_INPUT);
        gasInputHandler = InputHelper.getConstantInputHandler(gasTank);
        outputHandler = AMOutputHelper.getOutputHandler(mainOutputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE, secondaryOutputSlot, NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR);
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(gasTank = createGasTank(recipeCacheListener));
        return builder.build();
    }

    protected abstract IGasTank createGasTank(IContentsListener recipeCacheListener);

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @Override
    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(item -> containsRecipeAB(item, gasTank.getStack()), this::containsRecipeA, recipeCacheListener, 56, 17))
                .tracksWarnings(slot -> slot.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(gasSlot = GasInventorySlot.fillOrConvert(gasTank, this::getLevel, listener, 56, 53));
        builder.addSlot(mainOutputSlot = OutputInventorySlot.at(listener, 116, 35));
        builder.addSlot(secondaryOutputSlot = OutputInventorySlot.at(listener, 132, 35));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 31, 35));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        gasSlot.fillTankOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<PlantingRecipe, InputRecipeCache.ItemChemical<Gas, GasStack, PlantingRecipe>> getRecipeType() {
        return MoreMachineRecipeType.PLANTING.get();
    }

    @Override
    public @Nullable PlantingRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandler, gasInputHandler);
    }

    @Override
    public @NotNull CachedRecipe<PlantingRecipe> createNewCachedRecipe(@NotNull PlantingRecipe recipe, int cacheIndex) {
        return new FormulizedPlantingCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandler, gasInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(this::getBaselineMaxOperations);
    }

    protected abstract int getBaselineMaxOperations();

    @Override
    public boolean isConfigurationDataCompatible(BlockEntityType<?> tileType) {
        return super.isConfigurationDataCompatible(tileType) || MoreMachineUtils.isSameMMTypeFactory(getBlockType(), tileType);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getOffsetCapabilityIfEnabled(@NotNull Capability<T> capability, Direction side, @NotNull Vec3i offset) {
        if (this instanceof ITileEntityMekanismAccessor accessor) {
            if (capability == Capabilities.GAS_HANDLER) {
                return accessor.getGasHandlerManager().resolve(capability, side);
            } else if (EnergyCompatUtils.isEnergyCapability(capability)) {
                return accessor.getEnergyHandlerManager().resolve(capability, side);
            }
        }
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandlerManager.resolve(capability, side);
        }
        return getCapability(capability, side);
    }

    @Override
    public boolean isOffsetCapabilityDisabled(@NotNull Capability<?> capability, Direction side, @NotNull Vec3i offset) {
        if (!capability.isRegistered()) {
            return true;
        } else if (capability == Capabilities.GAS_HANDLER) {
            return notChemicalPort(side, offset);
        } else if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return notItemPort(side, offset);
        } else if (EnergyCompatUtils.isEnergyCapability(capability)) {
            return notEnergyPort(side, offset);
        }
        return notChemicalPort(side, offset) && notItemPort(side, offset) && notEnergyPort(side, offset);
    }

    private boolean notChemicalPort(Direction side, Vec3i offset) {
        if (offset.equals(new Vec3i(0, 1, 0))) {
            return side != Direction.UP;
        }
        return true;
    }

    private boolean notItemPort(Direction side, Vec3i offset) {
        if (offset.equals(new Vec3i(0, 1, 0))) {
            return side != Direction.UP;
        }
        return true;
    }

    private boolean notEnergyPort(Direction side, Vec3i offset) {
        if (offset.equals(new Vec3i(0, 1, 0))) {
            return side != Direction.UP;
        }
        return true;
    }

    public MachineEnergyContainer<BMPlantingStation> getEnergyContainer() {
        return energyContainer;
    }

    @ComputerMethod(methodDescription = ComputerConstants.DESCRIPTION_GET_ENERGY_USAGE)
    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }
}
