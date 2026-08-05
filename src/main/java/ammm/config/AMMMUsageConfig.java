package ammm.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class AMMMUsageConfig extends BaseMekanismConfig {
    private final ForgeConfigSpec configSpec;
    public final CachedFloatingLongValue astralCrafter;

    AMMMUsageConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Machine Energy Usage Config. This config is synced from server to client.").push("usage");
        astralCrafter = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).",
                "essentialCrafter", FloatingLong.createConst(50));

        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "astral-mekanism-machine-usage";
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
