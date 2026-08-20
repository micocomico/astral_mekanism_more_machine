package ammm.block.blockentity.normalfactory;

import ammm.block.blockentity.base.MekanismProgressFactory;
import ammm.block.blockentity.interf.ICrushingFactory;
import ammm.block.blockentity.interf.IEssentialCrusher;
import astral_mekanism.block.blockentity.base.BlockEntityProgressFactory;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedInputInventorySlot;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedOutputInventorySlot;
import astral_mekanism.block.blockentity.interf.IEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter;
import astral_mekanism.block.blockentity.normalfactory.BEEnergizedSmeltingFactory;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.cachedrecipe.EssentialSmeltingCachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.ItemInfuseOutput;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
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
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableEnum;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.TileEntityChemicalTank.GasMode;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CrushingFactorysub
        extends MekanismProgressFactory<ItemStackToItemStackRecipe, CrushingFactorysub>
        implements ICrushingFactory<CrushingFactorysub> {

    private PagedInputInventorySlot[] inputSlots;
    private PagedOutputInventorySlot[] outputSlots;
    private final IInputHandler<ItemStack>[] inputHandlers;
    private final IOutputHandler<ItemStack>[] outputHandlers;
    private FloatingLong energyUsed = FloatingLong.ZERO;

    @SuppressWarnings("unchecked")
    public CrushingFactorysub(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, 200, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM,
                TransmissionType.ENERGY);
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
    public @NotNull IMekanismRecipeTypeProvider<ItemStackToItemStackRecipe, InputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> getRecipeType() {
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
    public MachineEnergyContainer<CrushingFactorysub> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public CrushingFactorysub getSelf() {
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

    @NotNull
    @Override
    public List<Component> getInfo(@NotNull Upgrade upgrade) {
        return UpgradeUtils.getMultScaledInfo(this, upgrade);
    }

    @Override
    public FloatingLong getEnergyUsage() {
        return energyUsed;
    }

}
