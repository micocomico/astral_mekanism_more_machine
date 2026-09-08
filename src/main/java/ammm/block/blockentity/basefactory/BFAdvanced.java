package ammm.block.blockentity.basefactory;

import ammm.block.blockentity.base.MekanismRecipeFactory;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedInputInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.MathUtils;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.ItemStackConstantChemicalToItemStackCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.integration.computer.ComputerException;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.interfaces.IHasDumpButton;
import mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine;
import mekanism.common.upgrade.AdvancedMachineUpgradeData;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.StatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class BFAdvanced<BE extends MekanismRecipeFactory<ItemStackGasToItemStackRecipe, BE, InputRecipeCache.ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>>>
        extends MekanismRecipeFactory<ItemStackGasToItemStackRecipe, BE, InputRecipeCache.ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>>
        implements IHasDumpButton, IDoubleRecipeLookupHandler.ItemChemicalRecipeLookupHandler<Gas, GasStack, ItemStackGasToItemStackRecipe> {

    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    private static final Set<CachedRecipe.OperationTracker.RecipeError> GLOBAL_ERROR_TYPES = Set.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT
    );

    private static final int BASE_TICKS_REQUIRED = 200;

    public final ILongInputHandler<@NotNull GasStack> gasInputHandler;
    public final long[] usedSoFar;
    private double gasPerTickMeanMultiplier = 1;
    private long baseTotalUsage;

    public final ItemStackConstantChemicalToItemStackCachedRecipe.ChemicalUsageMultiplier gasUsageMultiplier;
    public final IInputHandler<ItemStack>[] inputHandlers;
    public final IOutputHandler<ItemStack>[] outputHandlers;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {"getChemical", "getChemicalCapacity", "getChemicalNeeded",
            "getChemicalFilledPercentage"}, docPlaceholder = "gas tank")
    public IGasTank gasTank;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getChemicalItem", docPlaceholder = "chemical item (extra) slot")
    public GasInventorySlot extraSlot;
    public PagedInputInventorySlot[] inputSlots;
    public PagedOutputInventorySlot[] outputSlots;
    public final int[] progress;

    @SuppressWarnings("unchecked")
    public BFAdvanced(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);
        this.progress = new int[tier.processes];
        Arrays.fill(progress, 0);
        gasInputHandler = InputHelper.getConstantInputHandler(gasTank);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.GAS, TransmissionType.ENERGY);
        if (allowExtractingChemical()) {
            configComponent.setupIOConfig(TransmissionType.GAS, gasTank, RelativeSide.RIGHT).setCanEject(false);
        } else {
            configComponent.setupInputConfig(TransmissionType.GAS, gasTank);
        }
        configComponent.setupItemIOConfig(Arrays.asList(inputSlots), Arrays.asList(outputSlots), energySlot, false);
        configComponent.getConfig(TransmissionType.ITEM).addSlotInfo(DataType.EXTRA, new InventorySlotInfo(true, true, extraSlot));
        configComponent.getConfig(TransmissionType.ITEM).setDataType(DataType.EXTRA, RelativeSide.BOTTOM);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
        baseTotalUsage = BASE_TICKS_REQUIRED;
        usedSoFar = new long[tier.processes];
        if (useStatisticalMechanics()) {
            //Note: Statistical mechanics works best by just using the mean gas usage we want to target
            // rather than adjusting the mean each time to try and reach a given target
            gasUsageMultiplier = (usedSoFar, operatingTicks) -> StatUtils.inversePoisson(gasPerTickMeanMultiplier);
        } else {
            gasUsageMultiplier = (usedSoFar, operatingTicks) -> {
                long baseRemaining = baseTotalUsage - usedSoFar;
                int remainingTicks = getTicksRequired() - operatingTicks;
                if (baseRemaining < remainingTicks) {
                    //If we already used more than we would need to use (due to removing speed upgrades or adding gas upgrades)
                    // then just don't use any gas this tick
                    return 0;
                } else if (baseRemaining == remainingTicks) {
                    return 1;
                }
                return Math.max(MathUtils.clampToLong(baseRemaining / (double) remainingTicks), 0);
            };
        }
        this.inputHandlers = new IInputHandler[tier.processes];
        this.outputHandlers = new IOutputHandler[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            inputHandlers[i] = InputHelper.getInputHandler(inputSlots[i], CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
            outputHandlers[i] = OutputHelper.getOutputHandler(outputSlots[i],
                    CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        }
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSideGasWithConfig(this::getDirection, this::getConfig);
        if (allowExtractingChemical()) {
            gasTank = ChemicalTankBuilder.GAS.create(TileEntityAdvancedElectricMachine.MAX_GAS * tier.processes, this::containsRecipeB,
                    markAllMonitorsChanged(listener));
        } else {
            gasTank = ChemicalTankBuilder.GAS.input(TileEntityAdvancedElectricMachine.MAX_GAS * tier.processes, this::containsRecipeB,
                    markAllMonitorsChanged(listener));
        }
        builder.addTank(gasTank);
        return builder.build();
    }

    @Override
    public @NotNull CachedRecipe<ItemStackGasToItemStackRecipe> createNewCachedRecipe(@NotNull ItemStackGasToItemStackRecipe recipe,
                                                                                      int cacheIndex) {
        return new ItemStackConstantChemicalToItemStackCachedRecipe<>(recipe, recheckAllRecipeErrors[cacheIndex], inputHandlers[cacheIndex], gasInputHandler,
                gasUsageMultiplier, used -> usedSoFar[cacheIndex] = used, outputHandlers[cacheIndex])
                .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(active -> setActiveState(active, cacheIndex))
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(this::getBaselineMaxOperations)
                .setOperatingTicksChanged(p -> progress[cacheIndex] = p);
    }

    protected abstract int getBaselineMaxOperations();

    @Override
    public @Nullable ItemStackGasToItemStackRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandlers[cacheIndex], gasInputHandler);
    }

    private boolean allowExtractingChemical() {
        return getRecipeType() == MekanismRecipeType.COMPRESSING;
    }

    public IGasTank getGasTank() {return gasTank;}

    @Override
    public int getWidthPerProcess() {
        return 18;
    }

    @Override
    public int getHeightPerProcess() {
        return 62;
    }

    @Override
    public int getSideSpaceWidth() {
        return 36;
    }

    @Override
    public void parseUpgradeData(@NotNull IUpgradeData upgradeData) {
        if (upgradeData instanceof AdvancedMachineUpgradeData data) {
            super.parseUpgradeData(upgradeData);
            gasTank.deserializeNBT(data.stored.serializeNBT());
            extraSlot.deserializeNBT(data.gasSlot.serializeNBT());
            System.arraycopy(data.usedSoFar, 0, usedSoFar, 0, data.usedSoFar.length);
        }
    }

    public void dump() {
        gasTank.setEmpty();
    }

    @ComputerMethod(requiresPublicSecurity = true, methodDescription = "Empty the contents of the gas tank into the environment")
    void dumpChemical() throws ComputerException {
        validateSecurityIsPublic();
        dump();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        extraSlot.fillTankOrConvert();
    }

    private boolean useStatisticalMechanics() {
        return getRecipeType() == MekanismRecipeType.INJECTING || getRecipeType() == MekanismRecipeType.PURIFYING;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED || upgrade == Upgrade.GAS && supportsUpgrade(Upgrade.GAS)) {
            if (useStatisticalMechanics()) {
                gasPerTickMeanMultiplier = MekanismUtils.getGasPerTickMeanMultiplier(this);
            } else {
                baseTotalUsage = MekanismUtils.getBaseUsage(this, BASE_TICKS_REQUIRED);
            }
        }
    }

    protected int getTicksRequired() {
        return 1;
    }

    @Override
    protected InventorySlotHelper addSlots(InventorySlotHelper builder, IContentsListener listener,
                                           IContentsListener updateSortingListener) {
        inputSlots = new PagedInputInventorySlot[tier.processes];
        outputSlots = new PagedOutputInventorySlot[tier.processes];
        builder.addSlot(extraSlot = GasInventorySlot.fillOrConvert(gasTank, this::getLevel, listener, 7, 62));
        extraSlot.setSlotType(ContainerSlotType.EXTRA);
        for (int i = 0; i < tier.processes; i++) {
            int index = i;
            int x = getXByIndex(index);
            int y = getY();
            int page = getPageByIndex(index);
            builder.addSlot(inputSlots[i] = PagedInputInventorySlot.at(this::containsRecipeA, () -> {
                updateSortingListener.onContentsChanged();
                recipeCacheLookupMonitors[index].onChange();
            }, x, y, page));
            builder.addSlot(outputSlots[i] = PagedOutputInventorySlot.at(updateSortingListener, x, y + 44, page));
        }
        return builder;
    }
}
