package astral_mekanism_more_machine.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class AMMMStorageConfig extends BaseMekanismConfig {

    private final ForgeConfigSpec configSpec;
    public final CachedFloatingLongValue astralCrafter;


    AMMMStorageConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Machine Energy Storage Config. This config is synced from server to client.").push("storage");
        astralCrafter = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).",
                "essentialCrafter", FloatingLong.createConst(Long.MAX_VALUE));
        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "astral-mekanism-machine-storage";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public Type getConfigType() {
        return Type.SERVER;
    }

    @Override
    public boolean addToContainer() {
        return false;
    }

}
