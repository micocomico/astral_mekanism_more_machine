package ammm.block.blockentity.machine.appliedmachine;

import ame_dynamics.config.AMEDConfig;
import ammm.block.blockentity.interf.applied.IAppliedDoubleToDoubleMachine;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.item.recipecard.FluidIngredientCardItem;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalDryingBasin;
import org.jetbrains.annotations.NotNull;

public class AppliedDryingBasin extends BEAppliedEnergizedMachine
        implements IAppliedDoubleToDoubleMachine {

    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot cardSlotB;
    private AEKey inputKeyA;
    private AEKey inputKeyB;
    private AEKey outputKeyA;
    private AEKey outputKeyB;
    private long inputAmountA;
    private long inputAmountB;
    private long outputAmountA;
    private long outputAmountB;

    public AppliedDryingBasin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, AMEDConfig.usage.essentialDryingBasin.get().multiply(100)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlotA = BasicInventorySlot.at(stack -> stack.getItem() instanceof FluidIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 55, 53));
        builder.addSlot(
                cardSlotB = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 73, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKeyA = null;
        inputKeyB = null;
        outputKeyA = null;
        outputKeyB = null;
        inputAmountA = 0;
        inputAmountB = 0;
        outputAmountA = 0;
        outputAmountB = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlotA.isEmpty() || cardSlotB.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack isA = cardSlotA.getStack();
        ItemStack isB = cardSlotB.getStack();
        if (isA.getItem() instanceof FluidIngredientCardItem cardItemA && isB.getItem() instanceof ItemIngredientCardItem cardItemB) {
            AEFluidKey keyA = cardItemA.getKey(isA);
            AEItemKey keyB = cardItemB.getKey(isB);
            if (hasLevel()) {
                FluidStack stackA = keyA != null ? keyA.toStack(0x7fffffff) : FluidStack.EMPTY;
                ItemStack stackB = keyB != null ? keyB.toStack(0x3fffffff) : ItemStack.EMPTY;
                level.getRecipeManager().getAllRecipesFor(getRecipeType())
                        .stream().filter(r -> r.getInputIngredient().test(stackB) && r.getInputFluid().getFluid() == stackA.getFluid()).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKeyA = keyA;
                            inputKeyB = keyB;
                            inputAmountA = r.getInputFluid().getAmount();
                            inputAmountB = 1;
                            ItemStack outputA = r.getOutputItem().orThrow();
                            FluidStack outputB = r.getOutputFluid();
                            outputKeyA = AEItemKey.of(outputA);
                            outputKeyB = AEFluidKey.of(outputB);
                            outputAmountA = outputA.getCount();
                            outputAmountB = outputB.getAmount();
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    protected RecipeType<RecipeMechanicalDryingBasin> getRecipeType(){
        return RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && (inputAmountA > 0 || inputAmountB > 0) && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(
                    Math.min(inputKeyA != null ? storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA : Long.MAX_VALUE,
                             inputKeyB != null ? storage.extract(inputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB : Long.MAX_VALUE),
                    Math.min(outputKeyA != null ? storage.insert(outputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountA : Long.MAX_VALUE,
                             outputKeyB != null ? storage.insert(outputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountB : Long.MAX_VALUE));
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                storage.extract(inputKeyA, operations * inputAmountA, Actionable.MODULATE, source);
                storage.extract(inputKeyB, operations * inputAmountB, Actionable.MODULATE, source);
                storage.insert(outputKeyA, operations * outputAmountA, Actionable.MODULATE, source);
                storage.insert(outputKeyB, operations * outputAmountB, Actionable.MODULATE, source);
                consumeEnergy(storage, source, operations);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    public AEKey getInputKeyA() {
        return inputKeyA;
    }

    public AEKey getInputKeyB() {
        return inputKeyB;
    }

    public AEKey getOutputKeyA() {
        return outputKeyA;
    }

    public AEKey getOutputKeyB() {
        return outputKeyB;
    }
}
