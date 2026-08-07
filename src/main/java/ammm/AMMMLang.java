package ammm;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum AMMMLang implements ILangEntry {

    DESCRIPTION_ASTRAL_CRAFTER("description", "astral_crafter"),
    DESCRIPTION_ENCHANTED_CRAFTER("description", "enchanted_crafter"),
    ITEM_GROUP("item_group", "modid"),;

    private final String key;

    AMMMLang(String type, String path) {
        this(Util.makeDescriptionId(type, AMMMConstants.rl(path)));
    }

    AMMMLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }

}
