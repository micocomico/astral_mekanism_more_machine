package ammm;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;

public enum AMMMLang implements ILangEntry {

    ITEM_GROUP("item_group", "modid"),
    FACTORIES_TAB_TITLE("tab_name", "factories");

    private final String key;

    AMMMLang(String type, String path) {
        this(Util.makeDescriptionId(type, AMMMConstants.rl(path)));
    }

    AMMMLang(String key) {
        this.key = key;
    }

    @Override
    @NotNull
    public String getTranslationKey() {
        return key;
    }

}
