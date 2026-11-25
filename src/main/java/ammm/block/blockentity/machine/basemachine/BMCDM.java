package ammm.block.blockentity.machine.basemachine;

import ammm.recipes.cached.FormulizedChemicalDemolitionCachedRecipe;
import fixdol.mekanismelements.api.recipes.ChemicalDemolitionRecipe;
import fixdol.mekanismelements.common.recipe.IMSRecipeTypeProvider;
import fixdol.mekanismelements.common.recipe.MSRecipeType;
import fixdol.mekanismelements.common.recipe.lookup.IMSDoubleRecipeLookupHandler;
import fixdol.mekanismelements.common.recipe.lookup.cache.MSInputRecipeCache;
import fixdol.mekanismelements.common.tile.prefab.MSTileEntityRecipeMachine;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.integration.computer.computercraft.ComputerConstants;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class BMCDM extends MSTileEntityRecipeMachine<ChemicalDemolitionRecipe> implements
        IMSDoubleRecipeLookupHandler.ItemChemicalRecipeLookupHandler<ChemicalDemolitionRecipe> {
    public static final CachedRecipe.OperationTracker.RecipeError NOT_ENOUGH_SPACE_SECOND_OUTPUT_ERROR = CachedRecipe.OperationTracker.RecipeError.create();
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            NOT_ENOUGH_SPACE_SECOND_OUTPUT_ERROR,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {"getChemicalInput", "getChemicalInputCapacity", "getChemicalInputNeeded",
            "getChemicalInputFilledPercentage"}, docPlaceholder = "chemical input tank")
    public IGasTank injectTank;

    private final IOutputHandler firstOutputHandler;
    private final IOutputHandler secondOutputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final ILongInputHandler<@NotNull GasStack> chemicalInputHandler;

    private MachineEnergyContainer<BMCDM> energyContainer;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getInputChemicalItem", docPlaceholder = "chemical input item slot")
    GasInventorySlot chemicalInputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "input slot")
    public InputInventorySlot inputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem", docPlaceholder = "output slot")
    OutputInventorySlot firstOutputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem", docPlaceholder = "output slot")
    OutputInventorySlot secondOutputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem", docPlaceholder = "energy slot")
    EnergyInventorySlot energySlot;

    public BMCDM(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, mekanism.common.lib.transmitter.TransmissionType.ITEM, mekanism.common.lib.transmitter.TransmissionType.GAS, mekanism.common.lib.transmitter.TransmissionType.ENERGY);
        ConfigInfo itemConfig = getConfig().setupItemIOConfig(List.of(inputSlot), List.of(firstOutputSlot, secondOutputSlot), energySlot, false);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(mekanism.common.tile.component.config.DataType.EXTRA,
                  new mekanism.common.tile.component.config.slot.InventorySlotInfo(true, true, chemicalInputSlot));
        }

        ConfigInfo chemicalConfig = getConfig().setupInputConfig(TransmissionType.GAS, injectTank);
        if (chemicalConfig != null) {
            chemicalConfig.setDataType(mekanism.common.tile.component.config.DataType.INPUT, mekanism.api.RelativeSide.LEFT);
            chemicalConfig.setDataType(mekanism.common.tile.component.config.DataType.INPUT, mekanism.api.RelativeSide.BACK);
            chemicalConfig.setEjecting(true);
        }

        ConfigInfo energyConfig = getConfig().setupInputConfig(TransmissionType.ENERGY, energyContainer);
        if (energyConfig != null) {
            for (mekanism.api.RelativeSide side : mekanism.api.RelativeSide.values()) {
                energyConfig.setDataType(mekanism.common.tile.component.config.DataType.INPUT, side);
            }
        }

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(getConfig(), TransmissionType.ITEM, TransmissionType.GAS)
                .setCanTankEject(tank -> tank != injectTank);

        itemInputHandler = InputHelper.getInputHandler(inputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
        chemicalInputHandler = InputHelper.getInputHandler(injectTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        firstOutputHandler = OutputHelper.getOutputHandler(firstOutputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        secondOutputHandler = OutputHelper.getOutputHandler(secondOutputSlot, NOT_ENOUGH_SPACE_SECOND_OUTPUT_ERROR);
    }

    @Override
    public net.minecraft.world.level.Level getHandlerWorld() {
        return getLevel();
    }

    protected void presetVariables() {
        super.presetVariables();
        injectTank = createGasTank();
        energyContainer = MachineEnergyContainer.input(this, recipeCacheLookupMonitor);
    }

    protected abstract IGasTank createGasTank();

    @NotNull
    @Override
    protected IChemicalTankHolder getInitialGasTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(injectTank);
        return builder.build();
    }

    protected boolean allowExtractingChemical() {
        return !useStatisticalMechanics();
    }

    protected boolean useStatisticalMechanics() {
        return false;
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer);
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(chemicalInputSlot = GasInventorySlot.fillOrConvert(injectTank, this::getLevel, recipeCacheListener, 8, 65));
        builder.addSlot(inputSlot = InputInventorySlot.at(item -> containsRecipeAB(item, injectTank.getStack()), this::containsRecipeA, recipeCacheListener, 28, 36))
                .tracksWarnings(slot -> slot.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(firstOutputSlot = OutputInventorySlot.at(recipeCacheListener, 116, 35));
        builder.addSlot(secondOutputSlot = OutputInventorySlot.at(recipeCacheListener, 132, 35));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, recipeCacheListener, 154, 62));
        chemicalInputSlot.setSlotOverlay(SlotOverlay.MINUS);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        chemicalInputSlot.fillTank();
    }

    @Override
    public IMSRecipeTypeProvider<ChemicalDemolitionRecipe, MSInputRecipeCache.ItemChemical<ChemicalDemolitionRecipe>> getMSRecipeType() {
        return MSRecipeType.CHEMICAL_DEMOLITION;
    }

    @Nullable
    @Override
    public ChemicalDemolitionRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandler, chemicalInputHandler);
    }

    @NotNull
    @Override
    public CachedRecipe<ChemicalDemolitionRecipe> createNewCachedRecipe(@NotNull ChemicalDemolitionRecipe recipe, int cacheIndex) {
        return new FormulizedChemicalDemolitionCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandler, chemicalInputHandler, firstOutputHandler, secondOutputHandler, 10)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(this::getBaselineMaxOperations);
    }

    protected abstract int getBaselineMaxOperations();

    @Override
    public void onCachedRecipeChanged(@Nullable mekanism.api.recipes.cache.CachedRecipe<ChemicalDemolitionRecipe> cachedRecipe, int cacheIndex) {
        clearRecipeErrors(cacheIndex);
    }

    public MachineEnergyContainer<BMCDM> getEnergyContainer() {
        return energyContainer;
    }

    @ComputerMethod(methodDescription = ComputerConstants.DESCRIPTION_GET_ENERGY_USAGE)
    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }
}