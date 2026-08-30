package ammm.block.blockentity.astralfactory;

import ammm.block.blockentity.basefactory.BFAdvanced;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedInputInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.MathUtils;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.ItemStackConstantChemicalToItemStackCachedRecipe;
import mekanism.api.recipes.cache.TwoInputCachedRecipe;
import mekanism.api.recipes.chemical.ItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.Mekanism;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.ComputerException;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
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
import mekanism.common.upgrade.CombinerUpgradeData;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AFCompressing
        extends BFAdvanced<ItemStackGasToItemStackRecipe, AFCompressing, InputRecipeCache.ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> implements
        IHasDumpButton,IDoubleRecipeLookupHandler.ItemChemicalRecipeLookupHandler<Gas, GasStack, ItemStackGasToItemStackRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    private static final Set<RecipeError> GLOBAL_ERROR_TYPES = Set.of(RecipeError.NOT_ENOUGH_ENERGY);

    private final ILongInputHandler<@NotNull GasStack> gasInputHandler;

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getChemicalItem", docPlaceholder = "chemical item (extra) slot")
    GasInventorySlot extraSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {"getChemical", "getChemicalCapacity", "getChemicalNeeded",
            "getChemicalFilledPercentage"}, docPlaceholder = "gas tank")
    IGasTank gasTank;
    private final long[] usedSoFar;
    private final ItemStackConstantChemicalToItemStackCachedRecipe.ChemicalUsageMultiplier gasUsageMultiplier;
    private PagedInputInventorySlot[] inputSlots;
    private PagedOutputInventorySlot[] outputSlots;
    private final IInputHandler<ItemStack>[] inputHandlers;
    private final IOutputHandler<ItemStack>[] outputHandlers;
    private int baselineMaxOperations = 0x7fffffff;

    @SuppressWarnings("unchecked")
    public AFCompressing(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);
        gasInputHandler = InputHelper.getConstantInputHandler(gasTank);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM,TransmissionType.ENERGY);
        configComponent.addSupported(TransmissionType.GAS);
        configComponent.setupIOConfig(TransmissionType.GAS, gasTank, RelativeSide.RIGHT).setCanEject(false);
        configComponent.setupItemIOConfig(Arrays.asList(inputSlots), Arrays.asList(outputSlots), energySlot, false);
        configComponent.getConfig(TransmissionType.ITEM).addSlotInfo(DataType.EXTRA, new InventorySlotInfo(true, true, extraSlot));
        configComponent.getConfig(TransmissionType.ITEM).setDataType(DataType.EXTRA, RelativeSide.BOTTOM);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
        usedSoFar = new long[tier.processes];
        gasUsageMultiplier = (usedSoFar, operatingTicks) -> 1;
        this.inputHandlers = new IInputHandler[tier.processes];
        this.outputHandlers = new IOutputHandler[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            inputHandlers[i] = InputHelper.getInputHandler(inputSlots[i], RecipeError.NOT_ENOUGH_INPUT);
            outputHandlers[i] = OutputHelper.getOutputHandler(outputSlots[i],
                    RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        }
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSideGasWithConfig(this::getDirection, this::getConfig);
            gasTank = ChemicalTankBuilder.GAS.create(Long.MAX_VALUE, this::containsRecipeB,markAllMonitorsChanged(listener));
        builder.addTank(gasTank);
        return builder.build();
    }

    @Override
    public IGasTank getGasTank() {
        return gasTank;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackGasToItemStackRecipe, InputRecipeCache.ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> getRecipeType() {
        return MekanismRecipeType.COMPRESSING;
    }

    @Override
    public @Nullable ItemStackGasToItemStackRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandlers[cacheIndex], gasInputHandler);
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
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(() -> baselineMaxOperations);
    }
    protected int getBaselineMaxOperations() {return 1;}

    @Override
    public MachineEnergyContainer<AFCompressing> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public AFCompressing getSelf() {
        return this;
    }

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
    protected InventorySlotHelper addSlots(InventorySlotHelper builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        inputSlots = new PagedInputInventorySlot[tier.processes];
        outputSlots = new PagedOutputInventorySlot[tier.processes];
        builder.addSlot(extraSlot = GasInventorySlot.fillOrConvert(gasTank, this::getLevel, listener, 7, 57));
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
