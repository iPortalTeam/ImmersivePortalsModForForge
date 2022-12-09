package qouteall.imm_ptl.peripheral;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mojang.serialization.Lifecycle;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.commands.PortalCommand;
import qouteall.imm_ptl.peripheral.platform_specific.PeripheralModEntry;
import qouteall.q_misc_util.MiscHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CommandStickItem extends Item {
    
    private static final ResourceKey<Registry<Data>> registryRegistryKey =
        ResourceKey.createRegistryKey(new ResourceLocation("immersive_portals:command_stick_type"));

    public static final DeferredRegister<Data> CommandStickData = DeferredRegister.create(new ResourceLocation("immersive_portals", "command_stick_type"), "imm_ptl");


    public static final Supplier<IForgeRegistry<Data>> REGISTRY = CommandStickData.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<Data> delete_portal = CommandStickData.register("delete_portal", () -> createData("delete_portal"));
    public static final RegistryObject<Data> remove_connected_portals = CommandStickData.register("remove_connected_portals", () -> createData("remove_connected_portals"));
    public static final RegistryObject<Data> eradicate_portal_cluster = CommandStickData.register("eradicate_portal_cluster", () -> createData("eradicate_portal_cluster"));
    public static final RegistryObject<Data> complete_bi_way_bi_faced_portal = CommandStickData.register("complete_bi_way_bi_faced_portal", () -> createData("complete_bi_way_bi_faced_portal"));
    public static final RegistryObject<Data> complete_bi_way_portal = CommandStickData.register("complete_bi_way_portal", () -> createData("complete_bi_way_portal"));
//    public static final RegistryObject<Data> bind_cluster = CommandStickData.register("bind_cluster", () -> createData("bind_cluster", "nbt {bindCluster:true}")); // TODO @Nick1st does this still exist?
    public static final RegistryObject<Data> move_portal_front = CommandStickData.register("move_portal_front", () -> createData("move_portal_front", "move_portal 0.5"));
    public static final RegistryObject<Data> move_portal_back = CommandStickData.register("move_portal_back", () -> createData("move_portal_back", "move_portal -0.5"));
    public static final RegistryObject<Data> move_portal_destination_front = CommandStickData.register("move_portal_destination_front", () -> createData("move_portal_destination_front", "move_portal_destination 0.5"));
    public static final RegistryObject<Data> move_portal_destination_back = CommandStickData.register("move_portal_destination_back", () -> createData("move_portal_destination_back", "move_portal_destination -0.5"));
    public static final RegistryObject<Data> rotate_x = CommandStickData.register("rotate_x", () -> createData("rotate_x", "rotate_portal_rotation_along x 15"));
    public static final RegistryObject<Data> rotate_y = CommandStickData.register("rotate_y", () -> createData("rotate_y", "rotate_portal_rotation_along y 15"));
    public static final RegistryObject<Data> rotate_z = CommandStickData.register("rotate_z", () -> createData("rotate_z", "rotate_portal_rotation_along z 15"));
    public static final RegistryObject<Data> make_unbreakable = CommandStickData.register("make_unbreakable", () -> createData("make_unbreakable", "nbt {unbreakable:true}"));
    public static final RegistryObject<Data> make_fuse_view = CommandStickData.register("make_fuse_view", () -> createData("make_fuse_view", "nbt {fuseView:true}"));
    public static final RegistryObject<Data> enable_pos_adjust = CommandStickData.register("enable_pos_adjust", () -> createData("enable_pos_adjust", "nbt {adjustPositionAfterTeleport:true}"));
    public static final RegistryObject<Data> disable_rendering_yourself = CommandStickData.register("disable_rendering_yourself", () -> createData("disable_rendering_yourself", "nbt {doRenderPlayer:false}"));
    public static final RegistryObject<Data> enable_isometric = CommandStickData.register("enable_isometric", () -> createData("enable_isometric", "debug isometric_enable 50"));
    public static final RegistryObject<Data> disable_isometric = CommandStickData.register("disable_isometric", () -> createData("disable_isometric", "debug isometric_disable"));

    public static final RegistryObject<Data> create_5_connected_rooms = CommandStickData.register("create_5_connected_rooms", () -> createData("create_5_connected_rooms", "create_connected_rooms roomSize 6 4 6 roomNumber 5"));
    public static final RegistryObject<Data> accelerate50 = CommandStickData.register("accelerate50", () -> createData("accelerate50", "debug accelerate 50"));
    public static final RegistryObject<Data> accelerate200 = CommandStickData.register("accelerate200", () -> createData("accelerate200", "debug accelerate 200"));
    public static final RegistryObject<Data> reverse_accelerate50 = CommandStickData.register("reverse_accelerate50", () -> createData("reverse_accelerate50", "debug accelerate -50"));
    public static final RegistryObject<Data> enable_gravity_change = CommandStickData.register("enable_gravity_change", () -> createData("enable_gravity_change", "nbt {teleportChangesGravity:true}"));

    public static final RegistryObject<Data> make_invisible = CommandStickData.register("make_invisible", () -> createData("make_invisible", "nbt {isVisible:false}"));
    public static final RegistryObject<Data> make_visible = CommandStickData.register("make_visible", () -> createData("make_visible", "nbt {isVisible:true}"));
    public static final RegistryObject<Data> disable_default_animation = CommandStickData.register("disable_default_animation", () -> createData("disable_default_animation", "nbt {defaultAnimation:{durationTicks:0}}"));
    public static final RegistryObject<Data> pause_animation = CommandStickData.register("pause_animation", () -> createData("pause_animation", "animation pause"));
    public static final RegistryObject<Data> resume_animation = CommandStickData.register("resume_animation", () -> createData("resume_animation", "animation resume"));
    public static final RegistryObject<Data> rotate_around_y = CommandStickData.register("rotate_around_y", () -> createData("rotate_around_y", "animation rotate_infinitely @s 0 1 0 1.0"));
    public static final RegistryObject<Data> rotate_randomly = CommandStickData.register("rotate_randomly", () -> createData("rotate_randomly", "animation rotate_infinitely_random"));
    public static final RegistryObject<Data> rotate_around_view = CommandStickData.register("rotate_around_view", () -> new CommandStickItem.Data("execute positioned 0.0 0.0 0.0 run portal animation rotate_infinitely @p ^0.0 ^0.0 ^1.0 1.7", "imm_ptl.command.rotate_around_view", Lists.newArrayList("imm_ptl.command_dest.rotate_around_view"), true));
    public static final RegistryObject<Data> expand_from_center = CommandStickData.register("expand_from_center", () -> createData("expand_from_center", "animation expand_from_center 20"));
    public static final RegistryObject<Data> clear_animation = CommandStickData.register("clear_animation", () -> createData("clear_animation", "animation clear"));
    public static final RegistryObject<Data> reset_scale = CommandStickData.register("reset_scale", () -> new CommandStickItem.Data("/scale set pehkui:base 1", "imm_ptl.command.reset_scale", Lists.newArrayList("imm_ptl.command_desc.reset_scale"),true));
    public static final RegistryObject<Data> long_reach = CommandStickData.register("long_reach", () -> new CommandStickItem.Data("/scale set pehkui:reach 5", "imm_ptl.command.long_reach", Lists.newArrayList("imm_ptl.command_desc.long_reach"), true));
    public static final RegistryObject<Data> night_vision = CommandStickData.register("night_vision", () -> new CommandStickItem.Data("/effect give @s minecraft:night_vision 9999 1 true", "imm_ptl.command.night_vision", List.of(), true));

    public static final RegistryObject<Data> goback = CommandStickData.register("goback", () -> createData("goback"));
    public static final RegistryObject<Data> show_wiki = CommandStickData.register("show_wiki", () -> createData("show_wiki", "wiki"));

    static List<Data> cmd_stick_data = new ArrayList<>();

    public static class Data {
        public final String command;
        public final String nameTranslationKey;
        public final List<String> descriptionTranslationKeys;
        
        public Data(
            String command, String nameTranslationKey, List<String> descriptionTranslationKeys, boolean addToMenu
        ) {
            this.command = command;
            this.nameTranslationKey = nameTranslationKey;
            this.descriptionTranslationKeys = descriptionTranslationKeys;

            if (addToMenu)
                cmd_stick_data.add(this);
        }
        
        public void serialize(CompoundTag tag) {
            tag.putString("command", command);
            tag.putString("nameTranslationKey", nameTranslationKey);
            ListTag listTag = new ListTag();
            for (String descriptionTK : descriptionTranslationKeys) {
                listTag.add(StringTag.valueOf(descriptionTK));
            }
            tag.put("descriptionTranslationKeys", listTag);
        }
        
        public static Data deserialize(CompoundTag tag) {
            return new Data(
                tag.getString("command"),
                tag.getString("nameTranslationKey"),
                tag.getList(
                        "descriptionTranslationKeys",
                        StringTag.valueOf("").getId()
                    )
                    .stream()
                    .map(tag1 -> ((StringTag) tag1).getAsString())
                    .collect(Collectors.toList()),
                    false
            );
        }
    }
    
    public static final MappedRegistry<Data> commandStickTypeRegistry = new MappedRegistry<>(
        registryRegistryKey, Lifecycle.stable(), null
    );
    
    public static void registerType(String id, Data data) {
        commandStickTypeRegistry.register(
            ResourceKey.create(
                registryRegistryKey, new ResourceLocation(id)
            ),
            data,
            Lifecycle.stable()
        );
    }

    public static Data createData(String name) {
        return createData(name, name);
    }
    private static Data createData(String name, String subCommand) {
        return new CommandStickItem.Data(
                "/portal " + subCommand,
                "imm_ptl.command." + name,
                Lists.newArrayList("imm_ptl.command_desc." + name), true
        );
    }

    public CommandStickItem(Properties settings) {
        super(settings);
    }
    
    // display enchantment glint
    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        doUse(player, player.getItemInHand(hand));
        return super.use(world, player, hand);
    }
    
    private void doUse(Player player, ItemStack stack) {
        if (player.level.isClientSide()) {
            return;
        }
        
        if (canUseCommand(player)) {
            Data data = Data.deserialize(stack.getOrCreateTag());
            
            CommandSourceStack commandSource = player.createCommandSourceStack().withPermission(2);
            
            Commands commandManager = MiscHelper.getServer().getCommands();
            
            String command = data.command;
            
            if (command.startsWith("/")) {
                // it seems not accepting "/" in the beginning
                command = command.substring(1);
            }
            
            commandManager.performPrefixedCommand(commandSource, command);
        }
        else {
            sendMessage(player, Component.literal("No Permission"));
        }
    }
    
    private static boolean canUseCommand(Player player) {
        if (IPGlobal.easeCommandStickPermission) {
            return true;// any player regardless of gamemode can use
        }
        else {
            return player.hasPermissions(2) || player.isCreative();
        }
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        
        Data data = Data.deserialize(stack.getOrCreateTag());
        
        Iterable<String> splitCommand = Splitter.fixedLength(40).split(data.command);
        
        for (String commandPortion : splitCommand) {
            tooltip.add(Component.literal(commandPortion).withStyle(ChatFormatting.GOLD));
        }
        
        for (String descriptionTranslationKey : data.descriptionTranslationKeys) {
            tooltip.add(Component.translatable(descriptionTranslationKey).withStyle(ChatFormatting.AQUA));
        }
        
        tooltip.add(Component.translatable("imm_ptl.command_stick").withStyle(ChatFormatting.GRAY));
    }
    
    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks) {
        if (this.allowedIn(group)) {
            cmd_stick_data.forEach(data -> {
                ItemStack stack = new ItemStack(PeripheralModEntry.COMMAND_STICK_ITEM.get());
                data.serialize(stack.getOrCreateTag());
                stacks.add(stack);
            });
        }
    }
    
    @Override
    public String getDescriptionId(ItemStack stack) {
        Data data = Data.deserialize(stack.getOrCreateTag());
        return data.nameTranslationKey;
    }
    
    public static void sendMessage(Player player, Component message) {
        ((ServerPlayer) player).sendSystemMessage(message);
    }
    
    public static void init() {
        PortalCommand.createCommandStickCommandSignal.connect((player, command) -> {
            ItemStack itemStack = new ItemStack(PeripheralModEntry.COMMAND_STICK_ITEM.get(), 1);
            Data data = new Data(
                command,
                command, new ArrayList<>(), false
            );
            data.serialize(itemStack.getOrCreateTag());
            
            player.getInventory().add(itemStack);
            player.inventoryMenu.broadcastChanges();
        });
    }
}
