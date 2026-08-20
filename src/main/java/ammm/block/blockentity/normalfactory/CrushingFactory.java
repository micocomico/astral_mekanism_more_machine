package ammm.block.blockentity.normalfactory;

import ammm.block.blockentity.base.MekanismProgressFactory;
import ammm.block.blockentity.base.MekanismRecipeFactory ;
import ammm.block.blockentity.interf.ICrushingFactory;
import ammm.block.blockentity.interf.IEssentialCrusher;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedInputInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.integration.AMEEmpowered;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrushingFactory
        extends MekanismProgressFactory<ItemStackToItemStackRecipe, CrushingFactory>
        implements ICrushingFactory<CrushingFactory> {

    private PagedInputInventorySlot[] inputSlots;
    private PagedOutputInventorySlot[] outputSlots;
    private final IInputHandler<ItemStack>[] inputHandlers;
    private final IOutputHandler<ItemStack>[] outputHandlers;
    @SuppressWarnings("unchecked")
    public CrushingFactory(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, 200,TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM,TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(Arrays.asList(inputSlots), Arrays.asList(outputSlots), energySlot, false);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
        this.inputHandlers = new IInputHandler[tier.processes];
        this.outputHandlers = new IOutputHandler[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            inputHandlers[i] = InputHelper.getInputHandler(inputSlots[i], RecipeError.NOT_ENOUGH_INPUT);
            outputHandlers[i] = OutputHelper.getOutputHandler(outputSlots[i],
                    IEssentialCrusher.NOT_ENOUGH_ITEM_OUTPUT_SPACE);
        }
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> getRecipeType() {
        return MekanismRecipeType.CRUSHING;
    }

    @Override
    public @Nullable ItemStackToItemStackRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandlers[cacheIndex]);
    }

    @Override
    public @NotNull CachedRecipe<ItemStackToItemStackRecipe> createNewCachedRecipe(@NotNull ItemStackToItemStackRecipe recipe,
                                                                        int cacheIndex) {
        return OneInputCachedRecipe.itemToItem(recipe, recheckAllRecipeErrors[cacheIndex], inputHandlers[cacheIndex],
                outputHandlers[cacheIndex])
                .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(active -> setActiveState(active, cacheIndex))
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setBaselineMaxOperations(this::getBaselineMaxOperations)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(p -> progress[cacheIndex] = p);
    }

    @Override
    public MachineEnergyContainer<CrushingFactory> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public CrushingFactory getSelf() {
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
    protected InventorySlotHelper addSlots(InventorySlotHelper builder, IContentsListener listener,
            IContentsListener updateSortingListener) {
        inputSlots = new PagedInputInventorySlot[tier.processes];
        outputSlots = new PagedOutputInventorySlot[tier.processes];
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
        }
        return builder;
    }

    @Override
    protected void sort() {
        PagedInputInventorySlot manySlot = Arrays.stream(inputSlots).reduce(inputSlots[0],
                (a, b) -> a.getCount() > b.getCount() ? a : b);
        if (manySlot.isEmpty()) {
            return;
        }
        List<PagedInputInventorySlot> emptySlots = Arrays.stream(inputSlots).filter(IInventorySlot::isEmpty).toList();
        if (emptySlots.isEmpty()) {
            return;
        }
        List<PagedInputInventorySlot> targetSlots = new ArrayList<>(emptySlots);
        targetSlots.add(0, manySlot);
        ItemStack stack = manySlot.getStack().copy();
        int size = targetSlots.size();
        int base = stack.getCount() / size;
        int left = stack.getCount() % size;
        for (int index = 0; index < size; index++) {
            targetSlots.get(index).setStack(stack.copyWithCount(index < left ? base + 1 : base));
        }
    }
}
