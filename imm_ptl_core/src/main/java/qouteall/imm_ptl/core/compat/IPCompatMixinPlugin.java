package qouteall.imm_ptl.core.compat;

import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class IPCompatMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    
    }
    
    @Override
    public String getRefMapperConfig() {
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        
        
        if (mixinClassName.contains("IrisSodium")) {
            boolean sodiumLoaded = LoadingModList.get().getModFileById("rubidium") != null;
            boolean irisLoaded = LoadingModList.get().getModFileById("oculus") != null;
            return sodiumLoaded && irisLoaded;
        }
        
        if (mixinClassName.contains("Iris")) {
            boolean irisLoaded = LoadingModList.get().getModFileById("oculus") != null;
            return irisLoaded;
        }
        
        if (mixinClassName.contains("Sodium")) {
            boolean sodiumLoaded = LoadingModList.get().getModFileById("rubidium") != null;
            return sodiumLoaded;
        }
        
        if (mixinClassName.contains("Flywheel")) {
            boolean flywheelLoaded = LoadingModList.get().getModFileById("flywheel") != null;
            return flywheelLoaded;
        }

        if (mixinClassName.contains("Lucent")) {
            boolean lucentLoaded = LoadingModList.get().getModFileById("lucent") != null;
//            if (lucentLoaded) {
//                asyncModChecks.add(LucentCompat::canAccessClientOnlyThread);
//            }
            return lucentLoaded;
        }
        
        return false;
    }
    
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    
    }
    
    @Override
    public List<String> getMixins() {
        return null;
    }
    
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    
    }
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    
    }

//    private static final List<BooleanSupplier> asyncModChecks = new ArrayList<>();
//
//    public static boolean asyncModCanAccessClientThreadOnlyMethod() {
//        for (BooleanSupplier check : asyncModChecks) {
//            if (check.getAsBoolean()) {
//                return true;
//            }
//        }
//        return false;
//    }
}
