package qouteall.imm_ptl.core.platform_specific;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import qouteall.imm_ptl.core.portal.*;
import qouteall.imm_ptl.core.portal.global_portals.GlobalTrackedPortal;
import qouteall.imm_ptl.core.portal.global_portals.VerticalConnectingPortal;
import qouteall.imm_ptl.core.portal.global_portals.WorldWrappingPortal;
import qouteall.imm_ptl.core.portal.nether_portal.GeneralBreakablePortal;
import qouteall.imm_ptl.core.portal.nether_portal.NetherPortalEntity;

public class IPRegistry {
    public static void registerMyDimensionsFabric() {
    }

    public static final RegistryObject<Block> NETHER_PORTAL_BLOCK = RegistryObject.create(new ResourceLocation(IPModEntry.MODID, "nether_portal_block"), ForgeRegistries.BLOCKS);

    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, handler -> handler.register(
            new ResourceLocation("imm_ptl_core", "nether_portal_block"),
            new PortalPlaceholderBlock(BlockBehaviour.Properties
                .of(Material.PORTAL)
                .noCollission()
                .sound(SoundType.GLASS)
                .strength(1.0f, 0)
                .noOcclusion()
                .noLootTable()
                .lightLevel((s)->15)
            )));
    }

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.ENTITY_TYPES, IPModEntry.MODID);

    public static final RegistryObject<EntityType<Portal>> PORTAL =
            ENTITY_TYPES.register("portal",
                    () -> EntityType.Builder.of(Portal::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .clientTrackingRange(96)
                            .build(IPModEntry.MODID + "portal"));

    public static final RegistryObject<EntityType<NetherPortalEntity>> NETHER_PORTAL_NEW =
            ENTITY_TYPES.register("nether_portal_new",
                    () -> EntityType.Builder.of(NetherPortalEntity::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .clientTrackingRange(96)
                            .build(IPModEntry.MODID + "nether_portal_new"));

    public static final RegistryObject<EntityType<EndPortalEntity>> END_PORTAL =
            ENTITY_TYPES.register("end_portal",
                    () -> EntityType.Builder.of(EndPortalEntity::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .clientTrackingRange(96)
                            .build(IPModEntry.MODID + "end_portal"));

    public static final RegistryObject<EntityType<Mirror>> MIRROR =
            ENTITY_TYPES.register("mirror",
                    () -> EntityType.Builder.of(Mirror::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .clientTrackingRange(96)
                            .build(IPModEntry.MODID + "mirror"));

    public static final RegistryObject<EntityType<BreakableMirror>> BREAKABLE_MIRROR =
            ENTITY_TYPES.register("breakable_mirror",
                    () -> EntityType.Builder.of(BreakableMirror::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .clientTrackingRange(96)
                            .build(IPModEntry.MODID + "breakable_mirror"));

    public static final RegistryObject<EntityType<GlobalTrackedPortal>> GLOBAL_TRACKED_PORTAL =
            ENTITY_TYPES.register("global_tracked_portal",
                    () -> EntityType.Builder.of(GlobalTrackedPortal::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .clientTrackingRange(96)
                            .build(IPModEntry.MODID + "global_tracked_portal"));

    public static final RegistryObject<EntityType<WorldWrappingPortal>> BORDER_PORTAL =
            ENTITY_TYPES.register("border_portal",
                    () -> EntityType.Builder.of(WorldWrappingPortal::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .clientTrackingRange(96)
                            .build(IPModEntry.MODID + "border_portal"));

    public static final RegistryObject<EntityType<VerticalConnectingPortal>> END_FLOOR_PORTAL =
            ENTITY_TYPES.register("end_floor_portal",
                    () -> EntityType.Builder.of(VerticalConnectingPortal::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .clientTrackingRange(96)
                            .build(IPModEntry.MODID + "end_floor_portal"));

    public static final RegistryObject<EntityType<GeneralBreakablePortal>> GENERAL_BREAKABLE_PORTAL =
            ENTITY_TYPES.register("general_breakable_portal",
                    () -> EntityType.Builder.of(GeneralBreakablePortal::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .clientTrackingRange(96)
                            .build(IPModEntry.MODID + "general_breakable_portal"));

    public static final RegistryObject<EntityType<LoadingIndicatorEntity>> LOADING_INDICATOR =
            ENTITY_TYPES.register("loading_indicator",
                    () -> EntityType.Builder.of(LoadingIndicatorEntity::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .clientTrackingRange(96)
                            .build(IPModEntry.MODID + "loading_indicator"));

    public static void registerEntities(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
    
    public static void registerChunkGenerators() {
        //it should not be serialized
//        Registry.register(
//            Registry.CHUNK_GENERATOR,
//            new Identifier("immersive_portals:error_terrain_gen"),
//            ErrorTerrainGenerator.codec
//        );
        
    }
}
