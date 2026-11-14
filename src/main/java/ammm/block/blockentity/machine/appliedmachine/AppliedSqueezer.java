package ammm.block.blockentity.machine.appliedmachine;

import ame_dynamics.config.AMEDConfig;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import mekanism.api.IContentsListener;
import mekanism.api.math.MathUtils;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalSqueezer;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class AppliedSqueezer extends BEAppliedEnergizedMachine {

    private BasicInventorySlot cardSlot;
    private AEKey inputKey;
    private AEKey outputKeyA;
    private AEKey outputKeyB;
    private AEKey outputKeyC;
    private AEKey outputKeyD;
    private long inputAmount;
    private long outputAmountA;
    private long outputAmountB;
    private long outputAmountC;
    private long outputAmountD;
    private float outputChanceA;
    private float outputChanceB;
    private float outputChanceC;
    private long energyAmount;

    public AppliedSqueezer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, AMEDConfig.usage.essentialSqueezer.get().multiply(200)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
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
        outputKeyA = null;
        outputKeyB = null;
        outputKeyC = null;
        outputKeyD = null;
        inputAmount = 0;
        outputAmountA = 0;
        outputAmountB = 0;
        outputAmountC = 0;
        outputAmountD = 0;
        energyAmount = 0;
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
                level.getRecipeManager().getAllRecipesFor(getRecipeType())
                        .stream().filter(r -> r.getInputIngredient().test(stack)).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKey = key;
                            inputAmount = 1;
                            NonNullList<RecipeSqueezer.IngredientChance> outputs = r.getOutputItems();
                            RecipeSqueezer.IngredientChance outputA = outputs.size() < 1 ? null : outputs.get(0);
                            RecipeSqueezer.IngredientChance outputB = outputs.size() < 2 ? null : outputs.get(1);
                            RecipeSqueezer.IngredientChance outputC = outputs.size() < 3 ? null : outputs.get(2);
                            FluidStack outputD = r.getOutputFluid();
                            outputKeyA = AEItemKey.of(outputA != null ? outputA.getIngredientFirst() : ItemStack.EMPTY);
                            outputKeyB = AEItemKey.of(outputB != null ? outputB.getIngredientFirst() : ItemStack.EMPTY);
                            outputKeyC = AEItemKey.of(outputC != null ? outputC.getIngredientFirst() : ItemStack.EMPTY);
                            outputKeyD = AEFluidKey.of(outputD);
                            outputAmountA = outputA != null ? outputA.getIngredientFirst().getCount() : 0;
                            outputAmountB = outputB != null ? outputB.getIngredientFirst().getCount() : 0;
                            outputAmountC = outputC != null ? outputC.getIngredientFirst().getCount() : 0;
                            outputAmountD = outputD.getAmount();
                            outputChanceA = outputA != null ? outputA.getChance() : 1;
                            outputChanceB = outputB != null ? outputB.getChance() : 1;
                            outputChanceC = outputC != null ? outputC.getChance() : 1;
                            energyAmount = AMEDConfig.usage.essentialSqueezer.get().multiply(r.getDuration())
                                    .divideToLong(MekanismConfig.general.forgeConversionRate.get());
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    protected RecipeType<RecipeMechanicalSqueezer> getRecipeType(){
        return RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && inputAmount > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(Math.min(
                    storage.extract(inputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmount,
                    storage.insert(outputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountA),
                    Math.min(outputKeyB != null ? storage.insert(outputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountB : Long.MAX_VALUE,
                             outputKeyC != null ? storage.insert(outputKeyC, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountC : Long.MAX_VALUE));
            operations = Math.min(operations, outputKeyD != null ? storage.insert(outputKeyD, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountD : Long.MAX_VALUE);
            operations = Math.min(operations,storage.insert(this.feKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / energyAmount);
            if (operations > 0) {
                Random rnd = new Random();
                storage.extract(inputKey, operations * inputAmount, Actionable.MODULATE, source);
                if (outputKeyA != null) storage.insert(outputKeyA, MathUtils.clampToLong((double) ((float) operations * (float) outputAmountA * outputChanceA + rnd.nextFloat(0.0F, 1.0F))), Actionable.MODULATE, source);
                if (outputKeyB != null) storage.insert(outputKeyB, MathUtils.clampToLong((double) ((float) operations * (float) outputAmountB * outputChanceB + rnd.nextFloat(0.0F, 1.0F))), Actionable.MODULATE, source);
                if (outputKeyC != null) storage.insert(outputKeyC, MathUtils.clampToLong((double) ((float) operations * (float) outputAmountC * outputChanceC + rnd.nextFloat(0.0F, 1.0F))), Actionable.MODULATE, source);
                storage.insert(outputKeyD, operations * outputAmountD, Actionable.MODULATE, source);
                storage.extract(this.feKey, operations * energyAmount, Actionable.MODULATE, source);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    public AEKey getInputKey() {
        return inputKey;
    }

    public AEKey getOutputKeyA() {
        return outputKeyA;
    }

    public AEKey getOutputKeyB() {
        return outputKeyB;
    }

    public AEKey getOutputKeyC() {
        return outputKeyC;
    }

    public AEKey getOutputKeyD() {
        return outputKeyD;
    }

}
