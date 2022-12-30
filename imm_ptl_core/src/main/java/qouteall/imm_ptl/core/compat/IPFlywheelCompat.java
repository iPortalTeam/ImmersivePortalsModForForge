package qouteall.imm_ptl.core.compat;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import qouteall.q_misc_util.Helper;

@OnlyIn(Dist.CLIENT)
public class IPFlywheelCompat {
    
    public static boolean isFlywheelPresent = false;
    
    public static void init(){
        if (ModList.get().isLoaded("flywheel")) {
            Helper.log("Flywheel is present");
        }
        
    }
    
}
