package ammm.block.blockentity.normalfactory;

import ammm.block.blockentity.basefactory.BFElectric;
import ammm.block.blockentity.basefactory.BFSawing;
import astral_mekanism.block.blockentity.elements.slot.paged.PagedInputInventorySlot;
import astral_mekanism.integration.AMEEmpowered;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.recipe.lookup.monitor.FactoryRecipeCacheLookupMonitor;
import mekanism.common.recipe.lookup.monitor.RecipeCacheLookupMonitor;
import mekanism.common.tile.interfaces.ISustainedData;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;

public class NFSawing extends BFSawing<NFSawing> implements ISustainedData {

    public NFSawing getSelf() {
        return this;
    }
    public MachineEnergyContainer<NFSawing> getEnergyContainer() {
        return energyContainer;
    }

    protected final int baseTicksRequired;
    private int ticksRequired;
    private boolean sorting;
    private boolean sortingNeeded = true;
    protected int baselineMaxOperations;

    public NFSawing(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        this.baseTicksRequired = 200;
        this.ticksRequired = this.baseTicksRequired;
        baselineMaxOperations = 1;
    }

    @Override
    protected RecipeCacheLookupMonitor<SawmillRecipe> createRecipeCacheLookupMonitor(int cacheIndex) {
        return new FactoryRecipeCacheLookupMonitor<>(this, cacheIndex, () -> sortingNeeded = true);
    }

    @Override
    protected IContentsListener getSecondLister(IContentsListener listener) {
        return () -> {
            listener.onContentsChanged();
            sortingNeeded = true;
        };
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        if (sortingNeeded && isSorting()) {
            sortingNeeded = false;
            sort();
        } else if (!sortingNeeded && CommonWorldTickHandler.flushTagAndRecipeCaches) {
            sortingNeeded = true;
        }
    }

    public void toggleSorting() {
        sorting = !isSorting();
        markForSave();
    }

    public boolean isSorting() {
        return sorting;
    }

    @Override
    protected int getTicksRequired() {
        return ticksRequired;
    }

    @Override
    public int getSavedOperatingTicks(int cacheIndex) {
        return progress[cacheIndex];
    }

    public IntConsumer getProgressSetter(int cacheIndex) {
        return p -> progress[cacheIndex] = p;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains(NBTConstants.PROGRESS, Tag.TAG_INT_ARRAY)) {
            int[] savedProgress = nbt.getIntArray(NBTConstants.PROGRESS);
            if (tier.processes != savedProgress.length) {
                Arrays.fill(progress, 0);
            }
            for (int i = 0; i < tier.processes && i < savedProgress.length; i++) {
                progress[i] = savedProgress[i];
            }
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.put(NBTConstants.PROGRESS, new IntArrayTag(Arrays.copyOf(progress, progress.length)));
    }

    @Override
    public void writeSustainedData(CompoundTag data) {
        data.putBoolean(NBTConstants.SORTING, isSorting());
    }

    @Override
    public void readSustainedData(CompoundTag data) {
        NBTUtils.setBooleanIfPresent(data, NBTConstants.SORTING, value -> sorting = value);
    }

    @Override
    public Map<String, String> getTileDataRemap() {
        Map<String, String> remap = new Object2ObjectOpenHashMap<>();
        remap.put(NBTConstants.SORTING, NBTConstants.SORTING);
        return remap;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 1 << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
        } else if (AMEEmpowered.empoweredIsLoaded()) {
            AMEEmpowered.recalculateUpgrades(getSelf(), upgrade, baseTicksRequired, v -> ticksRequired = v);
        } else if (upgrade == Upgrade.SPEED) {
            ticksRequired = MekanismUtils.getTicks(this, baseTicksRequired);
        }
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableBoolean.create(this::isSorting, v -> sorting = v));
        container.track(SyncableInt.create(this::getTicksRequired, v -> ticksRequired = v));
        container.track(SyncableInt.create(this::getBaselineMaxOperations, v -> baselineMaxOperations = v));
        container.trackArray(progress);
    }

    @Override
    public double getProgressScaled(int index) {
        return ((double) progress[index]) / ((double) ticksRequired);
    }

    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

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
