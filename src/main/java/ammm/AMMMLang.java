package ammm;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum AMMMLang implements ILangEntry {

    ITEM_GROUP("item_group", "modid"),
    TEST_GROUP("item_group", "testid"),;

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
