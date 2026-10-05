package ammm.block.blockentity.mashine.enchantedmachine;

import astral_mekanism.block.blockentity.base.BlockEntityRecipeMachine;
import astral_mekanism.block.blockentity.elements.ExtendedComponentEjector;
import astral_mekanism.block.blockentity.elements.energyContainer.EnergyRequiredRecipeMachineEnergyContainer;
import astral_mekanism.block.blockentity.interf.IAAEReactionChamber;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.cachedrecipe.AAEReactionCachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import astral_mekanism.generalrecipe.lookup.cache.recipe.AAEReactionRecipeCache;
import astral_mekanism.integration.AMEEmpowered;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.ItemFluidOutput;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class EMReactionChamber extends BlockEntityRecipeMachine<ReactionChamberRecipe>
        implements IAAEReactionChamber<EMReactionChamber> {

    private InputInventorySlot[] inputSlots;
    private OutputInventorySlot outputSlot;
    private EnergyInventorySlot energySlot;

    private BasicFluidTank inputTank;
    private BasicFluidTank outputTank;

    private EnergyRequiredRecipeMachineEnergyContainer<EMReactionChamber> energyContainer;

    private final IInputHandler<ItemStack>[] itemHandlers;
    private final IInputHandler<FluidStack> fluidHandler;
    private final IOutputHandler<ItemFluidOutput> outputHandler;

    private FloatingLong recipeEnergyRequierd = FloatingLong.ZERO;
    private FloatingLong lastEnergyUsage = FloatingLong.ZERO;
    private int baselineMaxOperations = 1;
    private int fluidTankCapacity = 1 * 20000;

    @SuppressWarnings("unchecked")
    public EMReactionChamber(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID,
                TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(List.<IInventorySlot>of(inputSlots), List.<IInventorySlot>of(outputSlot),
                energySlot, false);
        configComponent.setupIOConfig(TransmissionType.FLUID, inputTank, outputTank, RelativeSide.RIGHT);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new ExtendedComponentEjector(this, () -> 0x7fffffff)
                .setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.FLUID)
                .setCanFluidTankEject((tank, type) -> tank == outputTank && type.canOutput());
        itemHandlers = new IInputHandler[9];
        for (int index = 0; index < 9; index++) {
            itemHandlers[index] = InputHelper.getInputHandler(inputSlots[index], RecipeError.NOT_ENOUGH_INPUT);
        }
        fluidHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        outputHandler = AMOutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE, outputTank,
                RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        inputSlots = new InputInventorySlot[9];
        for (int i = 0; i < 9; i++) {
            int p = i;
            builder.addSlot(inputSlots[p] = InputInventorySlot.at(
                    stack -> containsItemOther(stack, getItems(), inputTank.getFluid()),
                    this::containsItem,
                    recipeCacheListener, p % 3 * 18 + 28, p / 3 * 18 + 17));
        }
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 116, 17));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTank = BasicFluidTank.input(fluidTankCapacity,
                stack -> containsFluidOther(getItems(), stack),
                this::containsFluid, recipeCacheListener));
        builder.addTank(outputTank = BasicFluidTank.output(fluidTankCapacity, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = EnergyRequiredRecipeMachineEnergyContainer.createInput(this, listener));
        return builder.build();
    }

    private ItemStack[] getSlotItems() {
        ItemStack[] result = new ItemStack[9];
        for (int index = 0; index < 9; index++) {
            result[index] = inputSlots[index].getStack();
        }
        return result;
    }

    private List<ItemStack> getItems() {
        return Arrays.stream(inputSlots).map(IInventorySlot::getStack).filter(stack -> !stack.isEmpty()).toList();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        lastEnergyUsage = recipeCacheLookupMonitor.updateAndProcess(energyContainer);
    }

    @Override
    public @NotNull IUnifiedRecipeTypeProvider<ReactionChamberRecipe, AAEReactionRecipeCache> getRecipeType() {
        return GeneralRecipeType.AAE_REACTION;
    }

    @Override
    public @Nullable ReactionChamberRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(List.of(itemHandlers), fluidHandler);
    }

    @Override
    public @NotNull ICachedRecipe<ReactionChamberRecipe> createNewCachedRecipe(@NotNull ReactionChamberRecipe recipe,
            int cacheIndex) {
        return new AAEReactionCachedRecipe(recipe, recheckAllRecipeErrors, itemHandlers, fluidHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setActive(this::setActive)
                .setOnFinish(this::markForSave)
                .setBaselineMaxOperations(this::getBaselineMaxOperations);
    }

    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            if (AMEEmpowered.isEmpoweredSpeed(upgrade) || upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
                baselineMaxOperations = ((1 << upgradeComponent.getUpgrades(Upgrade.SPEED)) + (2 << AMEEmpowered
                        .getEmpoweredSpeeds(this))) << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
            }
        } else if (upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 1 << (upgradeComponent.getUpgrades(Upgrade.SPEED)
                    + upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
        }
        fluidTankCapacity = 20000 * baselineMaxOperations;
    }

    @Override
    public void onCachedRecipeChanged(ICachedRecipe<ReactionChamberRecipe> cachedRecipe, int cacheIndex) {
        recipeEnergyRequierd = FloatingLong.create(cachedRecipe.getRecipe().getEnergy() * 5l);
    }

    @Override
    public IExtendedFluidTank getInputTank() {
        return inputTank;
    }

    @Override
    public IExtendedFluidTank getOutputTank() {
        return outputTank;
    }

    @Override
    public MachineEnergyContainer<EMReactionChamber> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public FloatingLong getRecipeEnergyRequired() {
        return recipeEnergyRequierd;
    }

    @Override
    public double getScaledProgress() {
        return getActive() ? 1 : 0;
    }

    @Override
    public FloatingLong getEnergyUsage() {
        return lastEnergyUsage;
    }

    @Override
    public double getProgressScaled() {
        return getScaledProgress();
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(this::getEnergyUsage, v -> lastEnergyUsage = v));
    }

}
