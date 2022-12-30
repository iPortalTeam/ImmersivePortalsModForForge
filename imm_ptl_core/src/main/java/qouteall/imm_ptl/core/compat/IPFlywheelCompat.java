package qouteall.imm_ptl.core.compat;

import com.demonwav.mcdev.annotations.Env;
import com.demonwav.mcdev.annotations.CheckEnv;
import net.minecraftforge.fml.ModList;
import qouteall.q_misc_util.Helper;

@CheckEnv(Env.CLIENT)
public class IPFlywheelCompat {
    
    public static boolean isFlywheelPresent = false;
    
    public static void init(){
        if (ModList.get().isLoaded("flywheel")) {
            Helper.log("Flywheel is present");
        }
        
    }
    
}
