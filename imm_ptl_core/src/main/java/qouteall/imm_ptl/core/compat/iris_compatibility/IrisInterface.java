package qouteall.imm_ptl.core.compat.iris_compatibility;

//DISABLED_COMPILEimport net.coderbot.iris.Iris;
//DISABLED_COMPILEimport net.coderbot.iris.pipeline.ShadowRenderer;
//DISABLED_COMPILEimport net.coderbot.iris.pipeline.WorldRenderingPipeline;
import net.minecraft.client.renderer.LevelRenderer;
import qouteall.q_misc_util.Helper;

import java.lang.reflect.Field;

public class IrisInterface {
    
    public static class Invoker {
        public boolean isIrisPresent() {
            return false;
        }
        
        public boolean isShaders() {
            return false;
        }
        
        public boolean isRenderingShadowMap() {
            return false;
        }
        
        public Object getPipeline(LevelRenderer worldRenderer) {
            return null;
        }
        
        // TODO check whether it's necessary
        public void setPipeline(LevelRenderer worldRenderer, Object pipeline) {
        
        }
        
        public void reloadPipelines() {}
    }
    
    public static class OnIrisPresent extends Invoker {
        
        private Field worldRendererPipelineField = Helper.noError(() -> {
            Field field = LevelRenderer.class.getDeclaredField("pipeline");
            field.setAccessible(true);
            return field;
        });
        
        @Override
        public boolean isIrisPresent() {
            return true;
        }

//DISABLED_COMPILE        @Override
//DISABLED_COMPILE        public boolean isShaders() {
//DISABLED_COMPILE            return Iris.getCurrentPack().isPresent();
//DISABLED_COMPILE        }
        
        @Override
        public boolean isRenderingShadowMap() {
            return false; //TODO RECOMMENT
//DISABLED_COMPILE            return ShadowRenderer.ACTIVE;
        }
        
        @Override
        public Object getPipeline(LevelRenderer worldRenderer) {
            return null; //TODO RECOMMENT
//DISABLED_COMPILE            return Helper.noError(() ->
//DISABLED_COMPILE                ((WorldRenderingPipeline) worldRendererPipelineField.get(worldRenderer))
//DISABLED_COMPILE            );
        }
        
        // the pipeline switching is unnecessary when using shaders
        // but still necessary with shaders disabled
        @Override
        public void setPipeline(LevelRenderer worldRenderer, Object pipeline) {
            Helper.noError(() -> {
                worldRendererPipelineField.set(worldRenderer, pipeline);
                return null;
            });
        }
        
        @Override
        public void reloadPipelines() {
//DISABLED_COMPILE            Iris.getPipelineManager().destroyPipeline();
        }
    }
    
    public static Invoker invoker = new Invoker();
}
