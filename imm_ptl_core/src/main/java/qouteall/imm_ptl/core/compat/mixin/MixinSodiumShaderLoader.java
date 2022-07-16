package qouteall.imm_ptl.core.compat.mixin;

import net.minecraft.resources.ResourceLocation;

//DISABLED_COMPILE@Mixin(value = ShaderLoader.class, remap = false)
public abstract class MixinSodiumShaderLoader {

    //DISABLED_COMPILE    @Shadow
    public static String getShaderSource(ResourceLocation name) {
        throw new RuntimeException();
    }
    
    /**
     * @author qouteall
     * @reason hard to inject
     */
//DISABLED_COMPILE    @Overwrite
//DISABLED_COMPILE    public static GlShader loadShader(ShaderType type, ResourceLocation name, ShaderConstants constants) {
//DISABLED_COMPILE        String shaderSource = getShaderSource(name);
//DISABLED_COMPILE        shaderSource = ShaderCodeTransformation.transform(
//DISABLED_COMPILE            type == ShaderType.VERTEX ? Program.Type.VERTEX : Program.Type.FRAGMENT,
//DISABLED_COMPILE            name.toString(), shaderSource
//DISABLED_COMPILE        );
//DISABLED_COMPILE        return new GlShader(type, name, ShaderParser.parseShader(shaderSource, constants));
//DISABLED_COMPILE    }
}
