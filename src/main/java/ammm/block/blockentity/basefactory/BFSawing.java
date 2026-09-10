package ammm.block.blockentity.basefactory;

import ammm.block.blockentity.base.MekanismRecipeFactory;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedInputInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import astral_mekanism.recipes.cachedRecipe.FormulizedSawingCachedRecipe;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.DoubleItemOutput;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.machine.TileEntityPrecisionSawmill;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class BFSawing<BE extends MekanismRecipeFactory<SawmillRecipe, BE, InputRecipeCache.SingleItem<SawmillRecipe>>>
        extends MekanismRecipeFactory<SawmillRecipe, BE, InputRecipeCache.SingleItem<SawmillRecipe>>
        implements ISingleRecipeLookupHandler.ItemRecipeLookupHandler<SawmillRecipe> {
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            TileEntityPrecisionSawmill.NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    private static final Set<CachedRecipe.OperationTracker.RecipeError> GLOBAL_ERROR_TYPES = Set.of(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY);

    public PagedInputInventorySlot[] inputSlots;
    public PagedOutputInventorySlot[] outputSlots;
    public PagedOutputInventorySlot[] secondaryoutputSlots;
    public final IInputHandler<ItemStack>[] inputHandlers;
    public final IOutputHandler<DoubleItemOutput>[] outputHandlers;
    public final int[] progress;
    protected final List<IInventorySlot> alloutputSlots;

    @SuppressWarnings("unchecked")
    public BFSawing(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);
        alloutputSlots = new ArrayList<>();
        this.progress = new int[tier.processes];
        Arrays.fill(progress, 0);
        alloutputSlots.addAll(Arrays.asList(outputSlots));
        alloutputSlots.addAll(Arrays.asList(secondaryoutputSlots));
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM,TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(Arrays.asList(inputSlots), alloutputSlots, energySlot, false);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
        this.inputHandlers = new IInputHandler[tier.processes];
        this.outputHandlers = new IOutputHandler[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            inputHandlers[i] = InputHelper.getInputHandler(inputSlots[i], CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
            outputHandlers[i] = AMOutputHelper.getOutputHandler(outputSlots[i], CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
                    secondaryoutputSlots[i],TileEntityPrecisionSawmill.NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR);
        }
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<SawmillRecipe, InputRecipeCache.SingleItem<SawmillRecipe>> getRecipeType() {
        return MekanismRecipeType.SAWING;
    }

    @Override
    public @Nullable SawmillRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandlers[cacheIndex]);
    }

    @Override
    public @NotNull CachedRecipe<SawmillRecipe> createNewCachedRecipe(@NotNull SawmillRecipe recipe,
                                                                                   int cacheIndex) {
        return new FormulizedSawingCachedRecipe(recipe, recheckAllRecipeErrors[cacheIndex], inputHandlers[cacheIndex],
                        outputHandlers[cacheIndex])
                .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(active -> setActiveState(active, cacheIndex))
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setOnFinish(this::markForSave)
                .setRequiredTicks(this::getTicksRequired)
                .setBaselineMaxOperations(this::getBaselineMaxOperations)
                .setOperatingTicksChanged(p -> progress[cacheIndex] = p);
    }

    protected int getTicksRequired() {
        return 1;
    }

    protected abstract int getBaselineMaxOperations();

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
    protected InventorySlotHelper addSlots(InventorySlotHelper builder, IContentsListener listener,
                                           IContentsListener updateSortingListener) {
        inputSlots = new PagedInputInventorySlot[tier.processes];
        outputSlots = new PagedOutputInventorySlot[tier.processes];
        secondaryoutputSlots = new PagedOutputInventorySlot[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            int index = i;
            int x = getXByIndex(index);
            int y = getY();
            int page = getPageByIndex(index);
            builder.addSlot(inputSlots[i] = PagedInputInventorySlot.at(this::containsRecipe, () -> {
                updateSortingListener.onContentsChanged();
                recipeCacheLookupMonitors[index].onChange();
            }, x, y, page));
            builder.addSlot(outputSlots[i] = PagedOutputInventorySlot.at(updateSortingListener, x, y + 44, page));
            builder.addSlot(secondaryoutputSlots[i] = PagedOutputInventorySlot.at(updateSortingListener, x, y + 64, page));
        }
        return builder;
    }
}
