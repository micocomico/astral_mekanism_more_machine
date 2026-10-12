package ammm.mixin.ame;

import astral_mekanism.block.blockentity.appliedmachine.BEAppliedElectrolyticSeparator;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedElectrolyticSeparator;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiElement;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GuiAppliedElectrolyticSeparator.class,remap = false)
public class GuiAppliedElectrolyticSeparatorMixin
        extends GuiMekanismTile<BEAppliedElectrolyticSeparator, MekanismTileContainer<BEAppliedElectrolyticSeparator>>{

    protected GuiAppliedElectrolyticSeparatorMixin(MekanismTileContainer<BEAppliedElectrolyticSeparator> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Redirect(method = "addGuiElements",at = @At(value = "INVOKE", target = "Lastral_mekanism/block/gui/appliedmachine/GuiAppliedElectrolyticSeparator;addRenderableWidget(Lmekanism/client/gui/element/GuiElement;)Lmekanism/client/gui/element/GuiElement;",ordinal = 1))
    protected GuiElement hage(GuiAppliedElectrolyticSeparator instance, GuiElement guiElement){
        return addRenderableWidget(new GuiMEKeySlot(this, 115, 24, tile::getMeStorage, tile::getFirstOutputKey));
    }

    @Redirect(method = "addGuiElements",at = @At(value = "INVOKE", target = "Lastral_mekanism/block/gui/appliedmachine/GuiAppliedElectrolyticSeparator;addRenderableWidget(Lmekanism/client/gui/element/GuiElement;)Lmekanism/client/gui/element/GuiElement;",ordinal = 2))
    protected GuiElement hugo(GuiAppliedElectrolyticSeparator instance, GuiElement guiElement){
        return addRenderableWidget(new GuiMEKeySlot(this, 115, 43, tile::getMeStorage, tile::getSecondOutputKey));
    }
}
