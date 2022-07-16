package qouteall.imm_ptl.core.compat;

//DISABLED_COMPILEimport me.andrew.gravitychanger.accessor.EntityAccessor;
//DISABLED_COMPILEimport me.andrew.gravitychanger.accessor.RotatableEntityAccessor;
//DISABLED_COMPILEimport me.andrew.gravitychanger.api.GravityChangerAPI;
//DISABLED_COMPILEimport me.andrew.gravitychanger.util.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import qouteall.imm_ptl.core.CHelper;
import qouteall.imm_ptl.core.McHelper;
import qouteall.q_misc_util.my_util.DQuaternion;

import javax.annotation.Nullable;

public class GravityChangerInterface {
    public static Invoker invoker = new Invoker();
    
    public static class Invoker {
        public boolean isGravityChangerPresent() {
            return false;
        }
        
        public Vec3 getEyeOffset(Entity entity) {
            return new Vec3(0, entity.getEyeHeight(), 0);
        }
        
        public Direction getGravityDirection(Player entity) {
            return Direction.DOWN;
        }
        
        public void setGravityDirection(Entity entity, Direction direction) {
            if (entity instanceof Player && entity.level.isClientSide()) {
                warnGravityChangerNotPresent();
            }
        }
        
        @Nullable
        public DQuaternion getExtraCameraRotation(Direction gravityDirection) {
            return null;
        }
        
        public Vec3 getWorldVelocity(Entity entity) {
            return entity.getDeltaMovement();
        }
        
        public void setWorldVelocity(Entity entity, Vec3 newVelocity) {
            entity.setDeltaMovement(newVelocity);
        }
        
        public Vec3 transformPlayerToWorld(Direction gravity, Vec3 vec3d) {
            return vec3d;
        }
        
        public Vec3 transformWorldToPlayer(Direction gravity, Vec3 vec3d) {
            return vec3d;
        }
    }
    
    private static boolean warned = false;
    
    @OnlyIn(Dist.CLIENT)
    private static void warnGravityChangerNotPresent() {
        if (!warned) {
            warned = true;
            CHelper.printChat(new TranslatableComponent("imm_ptl.missing_gravity_changer")
                .append(McHelper.getLinkText("https://github.com/qouteall/GravityChanger/releases/tag/v0.3.1"))
            );
        }
    }
    
    public static class OnGravityChangerPresent extends Invoker {
        
        @Override
        public boolean isGravityChangerPresent() {
            return true;
        }

        //DISABLED_COMPILE      @Override
        //DISABLED_COMPILE     public Vec3 getEyeOffset(Entity entity) {
        //DISABLED_COMPILE        if (entity instanceof Player player) {
//DISABLED_COMPILE                return GravityChangerAPI.getEyeOffset(player);
        //DISABLED_COMPILE         }
        //DISABLED_COMPILE       else {
        //DISABLED_COMPILE         return super.getEyeOffset(entity);
        //DISABLED_COMPILE      }
        //DISABLED_COMPILE  }

        //DISABLED_COMPILE    @Override
        //DISABLED_COMPILE    public Direction getGravityDirection(Player entity) {
//DISABLED_COMPILE            return ((EntityAccessor) entity).gravitychanger$getAppliedGravityDirection();
            //DISABLED_COMPILE    }
        
        @Override
        public void setGravityDirection(Entity entity, Direction direction) {
//DISABLED_COMPILE            ((RotatableEntityAccessor) entity).gravitychanger$setGravityDirection(direction, false);
        }
        
        @Nullable
        @Override
        public DQuaternion getExtraCameraRotation(Direction gravityDirection) {
            if (gravityDirection == Direction.DOWN) {
                return null;
            }
            return null; //TODO RECOMMENT

//DISABLED_COMPILE            return DQuaternion.fromMcQuaternion(
//DISABLED_COMPILE                RotationUtil.getWorldRotationQuaternion(gravityDirection)
//DISABLED_COMPILE            );
        }

        //DISABLED_COMPILE        @Override
        //DISABLED_COMPILE       public Vec3 getWorldVelocity(Entity entity) {
        //DISABLED_COMPILE          if (entity instanceof Player player) {
//DISABLED_COMPILE                return GravityChangerAPI.getWorldVelocity(player);
        //DISABLED_COMPILE         }
        //DISABLED_COMPILE        else {
        //DISABLED_COMPILE          return super.getWorldVelocity(entity);
        //DISABLED_COMPILE       }
        //DISABLED_COMPILE    }

        //DISABLED_COMPILE        @Override
        //DISABLED_COMPILE      public void setWorldVelocity(Entity entity, Vec3 newVelocity) {
        //DISABLED_COMPILE          if (entity instanceof Player player) {
        //DISABLED_COMPILE             GravityChangerAPI.setWorldVelocity(player, newVelocity);
        //DISABLED_COMPILE        }
        //DISABLED_COMPILE         else {
        //DISABLED_COMPILE             super.setWorldVelocity(entity, newVelocity);
        //DISABLED_COMPILE          }
        //DISABLED_COMPILE      }

        //DISABLED_COMPILE     @Override
        //DISABLED_COMPILE     public Vec3 transformPlayerToWorld(Direction gravity, Vec3 vec3d) {
        //DISABLED_COMPILE         return RotationUtil.vecPlayerToWorld(vec3d, gravity);
        //DISABLED_COMPILE     }

        //DISABLED_COMPILE    @Override
        //DISABLED_COMPILE      public Vec3 transformWorldToPlayer(Direction gravity, Vec3 vec3d) {
        //DISABLED_COMPILE          return RotationUtil.vecWorldToPlayer(vec3d, gravity);
        //DISABLED_COMPILE      }
    }
}
