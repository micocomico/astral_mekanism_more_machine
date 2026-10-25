package ammm.block.blockentity.mashine.appliedmachine;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.block.blockentity.interf.applied.IAppliedSingleToSingleMachine;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AppliedComposter extends BEAppliedEnergizedMachine
        implements IAppliedSingleToSingleMachine {

    private BasicInventorySlot cardSlot;
    private AEKey inputKey;
    private AEKey outputKey;
    private long inputAmount;

    public AppliedComposter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, 1);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlot = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 64, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKey = null;
        outputKey = null;
        inputAmount = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlot.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack is = cardSlot.getStack();
        if (is.getItem() instanceof ItemIngredientCardItem cardItem) {
            AEItemKey key = cardItem.getKey(is);
            if (key != null && hasLevel()) {
                ItemStack stack = key.toStack(0x3fffffff);
                if(ComposterBlock.COMPOSTABLES.containsKey(stack.getItem())){
                    inputKey = key;
                    inputAmount = (long) Math.floor(7 / ComposterBlock.COMPOSTABLES.getFloat(stack.getItem()));
                    outputKey = AEItemKey.of(Items.BONE_MEAL);
                } else {
                    removeRecipeInfo();
                }
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && inputAmount > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(
                    storage.extract(inputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmount,
                    storage.insert(outputKey, Long.MAX_VALUE, Actionable.SIMULATE, source));
            if (operations > 0) {
                storage.extract(inputKey, operations * inputAmount, Actionable.MODULATE, source);
                storage.insert(outputKey, operations, Actionable.MODULATE, source);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    public AEKey getInputKey() {
        return inputKey;
    }

    public AEKey getOutputKey() {
        return outputKey;
    }

}
