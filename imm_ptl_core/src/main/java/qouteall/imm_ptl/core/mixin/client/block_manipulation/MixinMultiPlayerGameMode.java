package qouteall.imm_ptl.core.mixin.client.block_manipulation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraftforge.network.NetworkDirection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import qouteall.imm_ptl.core.block_manipulation.BlockManipulationClient;
import qouteall.imm_ptl.core.ducks.IEClientPlayerInteractionManager;
import qouteall.imm_ptl.core.platform_specific.forge.networking.IPMessage;
import qouteall.imm_ptl.core.platform_specific.forge.networking.PlayerAction;
import qouteall.imm_ptl.core.platform_specific.forge.networking.RightClick;
import qouteall.q_misc_util.Helper;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinMultiPlayerGameMode implements IEClientPlayerInteractionManager {
    @Shadow
    @Final
    private ClientPacketListener connection;
    
    @Shadow
    @Final
    private Minecraft minecraft;
    
    @ModifyArg(
        method = "startPrediction",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"
        )
    )
    private Packet modifyPacketInStartPrediction(Packet<?> packet) {
        if (BlockManipulationClient.isContextSwitched) {
            if (packet instanceof ServerboundPlayerActionPacket playerActionPacket) {
                return IPMessage.INSTANCE.toVanillaPacket(new PlayerAction(BlockManipulationClient.remotePointedDim, playerActionPacket), NetworkDirection.PLAY_TO_SERVER);
            }
            else if (packet instanceof ServerboundUseItemOnPacket useItemOnPacket) {
                return IPMessage.INSTANCE.toVanillaPacket(new RightClick(BlockManipulationClient.remotePointedDim, useItemOnPacket), NetworkDirection.PLAY_TO_SERVER);
            }
            else {
                // TODO ServerboundUseItemPacket
                Helper.err("Unknown packet in startPrediction");
                return packet;
            }
        }
        else {
            return packet;
        }
    }
    
    @ModifyArg(
        method = "startDestroyBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"
        )
    )
    private Packet redirectSendInStartDestroyBlock(Packet packet) {
        if (BlockManipulationClient.isContextSwitched) {
            return IPMessage.INSTANCE.toVanillaPacket(new PlayerAction(BlockManipulationClient.remotePointedDim, (ServerboundPlayerActionPacket) packet), NetworkDirection.PLAY_TO_SERVER);
        }
        else {
            return packet;
        }
    }
    
    @ModifyArg(
        method = "stopDestroyBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"
        )
    )
    private Packet redirectSendInStopDestroyBlock(Packet packet) {
        if (BlockManipulationClient.isContextSwitched) {
            return IPMessage.INSTANCE.toVanillaPacket(new PlayerAction(BlockManipulationClient.remotePointedDim, (ServerboundPlayerActionPacket) packet), NetworkDirection.PLAY_TO_SERVER);
        }
        else {
            return packet;
        }
    }
    
    // TODO should inject releaseUsingItem?
    
    
}
