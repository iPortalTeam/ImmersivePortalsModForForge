package qouteall.imm_ptl.core.platform_specific;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.q_misc_util.Helper;

public class IPConfig {

    public static ForgeConfigSpec.BooleanValue enableWarning;
    public static ForgeConfigSpec.BooleanValue enableMirrorCreation;
    public static ForgeConfigSpec.IntValue maxPortalLayer;
    public static ForgeConfigSpec.BooleanValue sharedBlockMeshBufferOptimization;
    public static ForgeConfigSpec.BooleanValue lagAttackProof;
    public static ForgeConfigSpec.IntValue portalRenderLimit;
    public static ForgeConfigSpec.IntValue indirectLoadingRadiusCap;
    public static ForgeConfigSpec.BooleanValue enableCrossPortalSound;
    public static ForgeConfigSpec.BooleanValue compatibilityRenderMode;
    public static ForgeConfigSpec.BooleanValue doCheckGlError;
    public static ForgeConfigSpec.IntValue portalSearchingRange;
    public static ForgeConfigSpec.BooleanValue renderYourselfInPortal;
    public static ForgeConfigSpec.BooleanValue serverSideNormalChunkLoading;
    public static ForgeConfigSpec.BooleanValue teleportationDebug;
    public static ForgeConfigSpec.BooleanValue correctCrossPortalEntityRendering;
    public static ForgeConfigSpec.BooleanValue looseMovementCheck;
    public static ForgeConfigSpec.BooleanValue pureMirror;
    public static ForgeConfigSpec.BooleanValue enableAlternateDimensions;
    public static ForgeConfigSpec.BooleanValue reducedPortalRendering;
    public static ForgeConfigSpec.BooleanValue visibilityPrediction;
    public static ForgeConfigSpec.BooleanValue netherPortalOverlay;
    public static ForgeConfigSpec.IntValue scaleLimit;
    public static ForgeConfigSpec.BooleanValue easeCreativePermission;
    public static ForgeConfigSpec.BooleanValue easeCommandStickPermission;
    public static ForgeConfigSpec.BooleanValue enableDatapackPortalGen;
    public static ForgeConfigSpec.BooleanValue enableCrossPortalView;
    public static ForgeConfigSpec.BooleanValue enableClippingMechanism;
    public static ForgeConfigSpec.BooleanValue enableDepthClampForPortalRendering;
    public static ForgeConfigSpec.BooleanValue lightVanillaNetherPortalWhenCrouching;
    public static ForgeConfigSpec.BooleanValue enableNetherPortalEffect;
    public static ForgeConfigSpec.BooleanValue enableClientPerformanceAdjustment;
    public static ForgeConfigSpec.BooleanValue enableServerPerformanceAdjustment;
    public static ForgeConfigSpec.EnumValue<IPGlobal.NetherPortalMode> netherPortalMode;
    public static ForgeConfigSpec.EnumValue<IPGlobal.EndPortalMode> endPortalMode;
    public static ForgeConfigSpec.BooleanValue enableModelDataFix;
    public static ForgeConfigSpec.BooleanValue editGlobalDimensionStack;

    public static void register(ForgeConfigSpec.Builder builder) {
        builder.comment("Check the wiki at https://qouteall.fun/immptl/wiki/Config-Options for more information");
        enableWarning = builder.define("enableWarning", true);
        enableMirrorCreation = builder.define("enableMirrowCreation", true);
        maxPortalLayer = builder.defineInRange("maxPortalLayer", 5, 0, Integer.MAX_VALUE);
        sharedBlockMeshBufferOptimization = builder.define("sharedBlockMeshBufferOptimization", true);
        lagAttackProof = builder.define("lagAttackProof", true);
        portalRenderLimit = builder.defineInRange("portalRenderLimit", 200, 0, Integer.MAX_VALUE);
        indirectLoadingRadiusCap = builder.defineInRange("indirectLoadingRadiusCap", 8, 0, Integer.MAX_VALUE);
        enableCrossPortalSound = builder.define("enableCrossPortalSound", true);
        enableModelDataFix = builder.define("enableModelDataFix", true);
        compatibilityRenderMode = builder.define("compatibilityRenderMode", false);
        doCheckGlError = builder.define("doCheckGlError", false);
        portalSearchingRange = builder.defineInRange("portalSearchingRange", 128, 0, Integer.MAX_VALUE);
        renderYourselfInPortal = builder.define("renderYourselfInPortal", true);
        serverSideNormalChunkLoading = builder.define("serverSideNormalChunkLoading", true);
        teleportationDebug = builder.define("teleportationDebug", false);
        correctCrossPortalEntityRendering = builder.define("correctCrossPortalEntityRendering", true);
        looseMovementCheck = builder.define("looseMovementCheck", false);
        pureMirror = builder.define("pureMirror", false);
        enableAlternateDimensions = builder.define("enableAlternateDimensions", true);
        reducedPortalRendering = builder.define("reducedPortalRendering", false);
        visibilityPrediction = builder.define("visibilityPrediction", true);
        netherPortalOverlay = builder.define("netherPortalOverlay", false);
        scaleLimit = builder.defineInRange("scaleLimit", 30, Integer.MIN_VALUE, Integer.MAX_VALUE);
        easeCreativePermission = builder.define("easeCreativePermission", true);
        easeCommandStickPermission = builder.define("easeCommandStickPermission", false);
        enableDatapackPortalGen = builder.define("enableDatapackPortalGen", true);
        enableCrossPortalView = builder.define("enableCrossPortalView", true);
        enableClippingMechanism = builder.define("enableClippingMechanism", true);
        enableDepthClampForPortalRendering = builder.define("enableDepthClampForPortalRendering", false);
        lightVanillaNetherPortalWhenCrouching = builder.define("lightVanillaNetherPortalWhenCrouching", false);
        enableNetherPortalEffect = builder.define("enableNetherPortalEffect", true);
        enableClientPerformanceAdjustment = builder.define("enableClientPerformanceAdjustment", true);
        enableServerPerformanceAdjustment = builder.define("enableServerPerformanceAdjustment", true);
        netherPortalMode = builder.defineEnum("netherPortalMode", IPGlobal.NetherPortalMode.normal, IPGlobal.NetherPortalMode.values());
        endPortalMode = builder.defineEnum("endPortalMode", IPGlobal.EndPortalMode.normal, IPGlobal.EndPortalMode.values());
        editGlobalDimensionStack = builder.define("enableGlobalDimensionStack", false);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }

    @SubscribeEvent
    public static void onConfigChanged(ModConfigEvent.Reloading event) {
        Helper.log("IP Config Reloading");
        loadConfig();
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        loadConfig();
    }

    public static void loadConfig() {
        if (compatibilityRenderMode.get()) {
            IPGlobal.renderMode = IPGlobal.RenderMode.compatibility;
        }
        else {
            IPGlobal.renderMode = IPGlobal.RenderMode.normal;
        }
        IPGlobal.enableWarning = enableWarning.get();
        IPGlobal.enableMirrorCreation = enableMirrorCreation.get();
        IPGlobal.doCheckGlError = doCheckGlError.get();
        IPGlobal.maxPortalLayer = maxPortalLayer.get();
        IPGlobal.lagAttackProof = lagAttackProof.get();
        IPGlobal.portalRenderLimit = portalRenderLimit.get();
        IPGlobal.netherPortalFindingRadius = portalSearchingRange.get();
        IPGlobal.renderYourselfInPortal = renderYourselfInPortal.get();
        IPGlobal.activeLoading = serverSideNormalChunkLoading.get();
        IPGlobal.teleportationDebugEnabled = teleportationDebug.get();
        IPGlobal.correctCrossPortalEntityRendering = correctCrossPortalEntityRendering.get();
        IPGlobal.looseMovementCheck = looseMovementCheck.get();
        IPGlobal.pureMirror = pureMirror.get();
        IPGlobal.enableAlternateDimensions = enableAlternateDimensions.get();
        IPGlobal.indirectLoadingRadiusCap = indirectLoadingRadiusCap.get();
        IPGlobal.netherPortalMode = netherPortalMode.get();
        IPGlobal.endPortalMode = endPortalMode.get();
        IPGlobal.reducedPortalRendering = reducedPortalRendering.get();
        IPGlobal.offsetOcclusionQuery = visibilityPrediction.get();
        IPGlobal.netherPortalOverlay = netherPortalOverlay.get();
        IPGlobal.scaleLimit = scaleLimit.get();
        IPGlobal.easeCreativePermission = easeCreativePermission.get();
        IPGlobal.enableSharedBlockMeshBuffers = sharedBlockMeshBufferOptimization.get();
        IPGlobal.enableDatapackPortalGen = enableDatapackPortalGen.get();
        IPGlobal.enableCrossPortalView = enableCrossPortalView.get();
        IPGlobal.enableClippingMechanism = enableClippingMechanism.get();
        IPGlobal.lightVanillaNetherPortalWhenCrouching = lightVanillaNetherPortalWhenCrouching.get();
        IPGlobal.enableNetherPortalEffect = enableNetherPortalEffect.get();
        IPGlobal.enableClientPerformanceAdjustment = enableClientPerformanceAdjustment.get();
        IPGlobal.enableServerPerformanceAdjustment = enableServerPerformanceAdjustment.get();
        IPGlobal.enableCrossPortalSound = enableCrossPortalSound.get();
        IPGlobal.enableModelDataFix = enableModelDataFix.get();

        if (Boolean.TRUE.equals(enableDepthClampForPortalRendering.get())) {
            IPGlobal.enableDepthClampForPortalRendering = true;
        }

        IPGlobal.editGlobalDimensionStack = editGlobalDimensionStack.get();

        Helper.log("IP Config Applied");
    }
    
}
