package qouteall.imm_ptl.core.platform_specific;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.Validate;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.teleportation.ClientTeleportationManager;
import qouteall.q_misc_util.Helper;

import java.lang.reflect.Method;

public class RequiemCompat {
    private static boolean isRequiemPresent = false;
    private static Class class_RequiemPlayer;
    private static Class class_PossessionComponent;
    private static Method method_asPossessor;
    private static Method method_getPossessedEntity;
    
    public static boolean getIsRequiemPresent() {
        return isRequiemPresent;
    }
    
    public static void init() {
        isRequiemPresent = ModList.get().isLoaded("requiem");
        
        if (!isRequiemPresent) {
            return;
        }
        
        class_RequiemPlayer = Helper.noError(() ->
            Class.forName("ladysnake.requiem.api.v1.RequiemPlayer"));
        
        class_PossessionComponent = Helper.noError(() ->
            Class.forName("ladysnake.requiem.api.v1.possession.PossessionComponent"));
        
        method_asPossessor = Helper.noError(() ->
            class_RequiemPlayer.getDeclaredMethod("asPossessor"));
        
        method_getPossessedEntity = Helper.noError(() ->
            class_PossessionComponent.getDeclaredMethod("getPossessedEntity"));
        
        
    }
    
    public static Mob getPossessedEntity(Player player) {
        Validate.isTrue(isRequiemPresent);
        
        Object possessionComponent = Helper.noError(() -> method_asPossessor.invoke(player));
        Object possessedEntity = Helper.noError(() ->
            method_getPossessedEntity.invoke(possessionComponent));
        return (Mob) possessedEntity;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerTeleportedClient() {
        if (!isRequiemPresent) {
            return;
        }
        
        LocalPlayer player = Minecraft.getInstance().player;
        Mob possessedEntity = getPossessedEntity(player);
        if (possessedEntity != null) {
            if (possessedEntity.level() != player.level()) {
                Helper.log("Move Requiem Possessed Entity at Client");
                ClientTeleportationManager.moveClientEntityAcrossDimension(
                    possessedEntity,
                    ((ClientLevel) player.level()),
                    player.position()
                );
            }
        }
    }
    
    public static void onPlayerTeleportedServer(ServerPlayer player) {
        if (!isRequiemPresent) {
            return;
        }
        
        Mob possessedEntity = getPossessedEntity(player);
        if (possessedEntity != null) {
            if (possessedEntity.level() != player.level()) {
                Helper.log("Move Requiem Posessed Entity at Server");
                IPGlobal.serverTeleportationManager.changeEntityDimension(
                    possessedEntity,
                    player.level().dimension(),
                    McHelper.getEyePos(player),
                    false
                );
            }
        }
    }
    
    
}
