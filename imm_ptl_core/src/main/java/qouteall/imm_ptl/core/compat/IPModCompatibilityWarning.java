package qouteall.imm_ptl.core.compat;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import qouteall.imm_ptl.core.CHelper;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.IPMcHelper;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.my_util.MyTaskList;

import java.util.List;

public class IPModCompatibilityWarning {
    
    // I do not put these mods into "breaks" so that compatibility can be debugged
    
    private static final List<ModInfo> incompatibleMods = Lists.newArrayList(
        new ModInfo("mcxr-core", "MCXR"),
        new ModInfo("taterzens", "Taterzens"),
        new ModInfo("altoclef", "Altoclef"),
//        new ModInfo("bitsandchisels", "Bits & Chisels"),
        new ModInfo("tweakeroo", "Tweakeroo"),
        new ModInfo("replaymod", "Replay Mod"),
        new ModInfo("twilightforest", "Twilight Forest"),
        new ModInfo("c2me", "C2ME")
    );
    
    private static final List<ModInfo> maybeIncompatibleMods = Lists.newArrayList(
        new ModInfo("physicsmod", "Physics Mod"),
//        new ModInfo("dashloader", "DashLoader"),
        new ModInfo("cameraoverhaul", "Camera Overhaul"),
        new ModInfo("figura", "Figura"),
        new ModInfo("dimthread", "Dimensional Threading"),
        new ModInfo("requiem", "Requiem"),
        new ModInfo("vmp", "VMP"),
        new ModInfo("modern_industrialization", "Modern Industrialization"),
        new ModInfo("create", "Create")
    );
    
    public static record ModInfo(String modId, String modName) {
    }
    
    public static void initDedicatedServer() {
        for (ModInfo mod : incompatibleMods) {
            if (ModList.get().isLoaded(mod.modId)) {
                Helper.err(String.format(
                    "WARNING: This mod is incompatible with Immersive Portals: %s(%s)",
                    mod.modName, mod.modId
                ));
            }
        }
        
        for (ModInfo mod : maybeIncompatibleMods) {
            if (ModList.get().isLoaded(mod.modId)) {
                Helper.err(String.format(
                    "WARNING: This mod is maybe incompatible with Immersive Portals: %s(%s)",
                    mod.modName, mod.modId
                ));
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void initClient() {
        for (ModInfo mod : incompatibleMods) {
            if (ModList.get().isLoaded(mod.modId)) {
                IPGlobal.clientTaskList.addTask(MyTaskList.withDelayCondition(
                    () -> Minecraft.getInstance().level == null,
                    MyTaskList.oneShotTask(() -> {
                        CHelper.printChat(new TextComponent(
                            String.format(
                                "WARNING: Immersive Portals mod is incompatible with mod %s(%s) . Major issues will occur. You should uninstall one of the two mods. (If the two mods become compatible now, report it to qouteall.)",
                                mod.modName, mod.modId
                            )
                        ).withStyle(ChatFormatting.RED));
                    })
                ));
                
            }
        }
        
        for (ModInfo mod : maybeIncompatibleMods) {
            if (ModList.get().isLoaded(mod.modId)) {
                IPGlobal.clientTaskList.addTask(MyTaskList.withDelayCondition(
                    () -> Minecraft.getInstance().level == null,
                    MyTaskList.oneShotTask(() -> {
                        String warningMessage = String.format(
                            "WARNING: Immersive Portals mod maybe has compatibility issues with mod %s(%s).",
                            mod.modName, mod.modId
                        );
                        if (IPGlobal.enableWarning) {
                            CHelper.printChat(
                                new TextComponent(warningMessage).withStyle(ChatFormatting.RED)
                                    .append(IPMcHelper.getDisableWarningText())
                            );
                        }
                        Helper.err(warningMessage);
                    })
                ));
            }
        }
        
        IPGlobal.clientTaskList.addTask(MyTaskList.withDelayCondition(
            () -> Minecraft.getInstance().level == null,
            MyTaskList.oneShotTask(() -> {
                //DISABLED_COMPILE            if (IPGlobal.enableWarning && !FabricLoader.getInstance().isDevelopmentEnvironment()) {
                //DISABLED_COMPILE            List<ModContainer> topLevelMods = FabricLoader.getInstance().getAllMods().stream()
                //DISABLED_COMPILE                .filter(modContainer -> modContainer.getContainingMod().isEmpty())
                //DISABLED_COMPILE               .collect(Collectors.toList());

                //DISABLED_COMPILE            if (topLevelMods.size() > 20) {
                //DISABLED_COMPILE                 CHelper.printChat(new TextComponent(
                //DISABLED_COMPILE                     "[Immersive Portals] WARNING: You are using many mods. It's likely that one of them has compatibility issues with Immersive Portals. " +
                //DISABLED_COMPILE                        "If you are sure that there is no compatibility issue, disable this warning."
                //DISABLED_COMPILE                ).withStyle(ChatFormatting.RED).append(IPMcHelper.getDisableWarningText()));
                //DISABLED_COMPILE             }
                //DISABLED_COMPILE         }
            })
        ));
        
    }
}
