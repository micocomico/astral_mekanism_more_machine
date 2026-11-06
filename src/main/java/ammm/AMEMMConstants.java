package ammm;


import net.minecraft.resources.ResourceLocation;

public class AMEMMConstants {
    public static final String MODID = "ammm";

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation reRL(ResourceLocation location, String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path + location.getNamespace() + "/" + location.getPath());
    }
}
