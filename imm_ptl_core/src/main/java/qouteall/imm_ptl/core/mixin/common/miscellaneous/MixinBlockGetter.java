package qouteall.imm_ptl.core.mixin.common.miscellaneous;

import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockGetter.class)
public interface MixinBlockGetter {
	
//	@ModifyVariable( //TODO Reenable this
//		method = "traverseBlocks",
//		at = @At("HEAD"),
//		argsOnly = true,
//		index = 1
//	)
//	private static <T, C> Vec3 onTraverseBlocks(
//		Vec3 originalArgument,
//		Vec3 from, Vec3 _to, C context,
//		BiFunction<C, BlockPos, T> tester, Function<C, T> onFail
//	) {
//		if (from.distanceToSqr(_to) > (256 * 256)) {
//			IPMcHelper.limitedLogger.invoke(() -> {
//				Helper.err("raycast too far");
//				new Throwable().printStackTrace();
//			});
//			return _to.subtract(from).normalize().scale(30).add(from);
//		}
//		return _to;
//	}
	
}
