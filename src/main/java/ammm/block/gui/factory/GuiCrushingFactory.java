package ammm.block.gui.factory;

import ammm.block.blockentity.interf.ICrushingFactory;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import astral_mekanism.block.blockentity.interf.IEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.gui.element.PagedGuiProgress;
import astral_mekanism.block.gui.factory.GuiAstralMekanismFactory;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.GuiGasMode;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiInfusionGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.recipe.impl.CrushingIRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class GuiCrushingFactory<BE extends BlockEntityRecipeFactory<CrushingIRecipe, BE> & ICrushingFactory<BE>>
        extends GuiAstralMekanismFactory<BE> {

    public GuiCrushingFactory(ContainerAstralMekanismFactory<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getEnergyUsage));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), imageWidth - 12, 16)
                .warning(WarningType.NOT_ENOUGH_ENERGY,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY, 0)));
        for (int index = 0; index < tile.tier.processes; index++) {
            int page = tile.getPageByIndex(index);
            int x = tile.getXByIndex(index) + 4;
            int y = tile.getY() + 20;
            int cacheIndex = index;
            addRenderableWidget(
                    new PagedGuiProgress(() -> tile.getProgressScaled(cacheIndex), ProgressType.DOWN, this, x, y, page))
                    .jeiCategories(MekanismJEIRecipeType.CRUSHING);
        }
    }
}
