package qouteall.imm_ptl.core.portal.custom_portal_gen.form;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;
import qouteall.imm_ptl.core.portal.PortalExtension;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.imm_ptl.core.portal.custom_portal_gen.CustomPortalGeneration;
import qouteall.imm_ptl.core.portal.custom_portal_gen.SimpleBlockPredicate;
import qouteall.imm_ptl.core.portal.nether_portal.BlockPortalShape;
import qouteall.imm_ptl.core.portal.nether_portal.GeneralBreakablePortal;
import qouteall.imm_ptl.core.portal.nether_portal.NetherPortalGeneration;
import qouteall.q_misc_util.my_util.IntBox;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class FlippingFloorSquareForm extends PortalGenForm {
    
    public static final ListCodec<Block> blockListCodec = new ListCodec<>(Registry.BLOCK.byNameCodec());
    
    public static final Codec<FlippingFloorSquareForm> codec = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("length").forGetter(o -> o.length),
            
            SimpleBlockPredicate.codec.fieldOf("frame_block").forGetter(o -> o.frameBlock),
            SimpleBlockPredicate.codec.fieldOf("area_block").forGetter(o -> o.areaBlock),
            SimpleBlockPredicate.codec.optionalFieldOf("up_frame_block", SimpleBlockPredicate.pass)
                .forGetter(o -> o.upFrameBlock),
            SimpleBlockPredicate.codec.optionalFieldOf("bottom_block", SimpleBlockPredicate.pass)
                .forGetter(o -> o.bottomBlock)
        
        ).apply(instance, instance.stable(FlippingFloorSquareForm::new));
    });
    
    public final int length;
    public final SimpleBlockPredicate frameBlock;
    public final SimpleBlockPredicate areaBlock;
    public final SimpleBlockPredicate upFrameBlock;
    public final SimpleBlockPredicate bottomBlock;
    
    public FlippingFloorSquareForm(
        int length,
        SimpleBlockPredicate frameBlock, SimpleBlockPredicate areaBlock,
        SimpleBlockPredicate upFrameBlock, SimpleBlockPredicate bottomBlock
    ) {
        this.length = length;
        this.frameBlock = frameBlock;
        this.areaBlock = areaBlock;
        this.upFrameBlock = upFrameBlock;
        this.bottomBlock = bottomBlock;
    }
    
    @Override
    public Codec<? extends PortalGenForm> getCodec() {
        return codec;
    }
    
    @Override
    public PortalGenForm getReverse() {
        return this;
    }
    
    @Override
    public boolean perform(
        CustomPortalGeneration cpg,
        ServerLevel fromWorld, BlockPos startingPos,
        ServerLevel toWorld,
        @Nullable Entity triggeringEntity
    ) {
        Predicate<BlockState> areaPredicate = areaBlock;
        Predicate<BlockState> framePredicate = frameBlock;
        Predicate<BlockState> bottomPredicate = bottomBlock;
        
        if (!areaPredicate.test(fromWorld.getBlockState(startingPos))) {
            return false;
        }
        
        if (!bottomPredicate.test(fromWorld.getBlockState(startingPos.below()))) {
            return false;
        }
        
        BlockPortalShape fromShape = BlockPortalShape.findArea(
            startingPos,
            Direction.Axis.Y,
            blockPos -> areaPredicate.test(fromWorld.getBlockState(blockPos)),
            blockPos -> framePredicate.test(fromWorld.getBlockState(blockPos))
        );
        
        if (fromShape == null) {
            return false;
        }
        
        if (!checkFromShape(fromWorld, fromShape)) {
            return false;
        }
        
        BlockPos areaSize = fromShape.innerAreaBox.getSize();
        
        BlockPos toPos = cpg.mapPosition(fromShape.innerAreaBox.l, fromWorld, toWorld);
        
        IntBox placingBox = findPortalPlacement(toWorld, areaSize, toPos);
        
        BlockPos offset = placingBox.l.subtract(fromShape.innerAreaBox.l);
        BlockPortalShape toShape = fromShape.getShapeWithMovedAnchor(
            fromShape.anchor.offset(offset)
        );
        
        // clone the frame into the destination
        fromShape.frameAreaWithoutCorner.forEach(fromWorldPos -> {
            BlockPos toWorldPos = fromWorldPos.offset(offset);
            toWorld.setBlockAndUpdate(toWorldPos, fromWorld.getBlockState(fromWorldPos));
            toWorld.setBlockAndUpdate(toWorldPos.above(), fromWorld.getBlockState(fromWorldPos.above()));
        });
        NetherPortalGeneration.fillInPlaceHolderBlocks(fromWorld, fromShape);
        NetherPortalGeneration.fillInPlaceHolderBlocks(toWorld, toShape);
        
        GeneralBreakablePortal[] portals = createPortals(fromWorld, toWorld, fromShape, toShape);
        
        cpg.onPortalsGenerated(portals);
        
        return true;
    }
    
    public boolean checkFromShape(ServerLevel fromWorld, BlockPortalShape fromShape) {
        boolean areaSizeTest = BlockPortalShape.isSquareShape(fromShape, length);
        if (!areaSizeTest) {
            return false;
        }
        
        return fromShape.frameAreaWithoutCorner.stream().allMatch(
            blockPos -> (upFrameBlock).test(fromWorld.getBlockState(blockPos.above()))
        ) && fromShape.area.stream().allMatch(
            blockPos -> (bottomBlock).test(fromWorld.getBlockState(blockPos.below()))
        );
    }
    
    public static IntBox findPortalPlacement(ServerLevel toWorld, BlockPos areaSize, BlockPos toPos) {
        return IntStream.range(toPos.getX() - 8, toPos.getX() + 8).boxed()
            .flatMap(x -> IntStream.range(toPos.getZ() - 8, toPos.getZ() + 8).boxed()
                .flatMap(z -> IntStream.range(
                    McHelper.getMinY(toWorld) + 5,
                    McHelper.getMaxContentYExclusive(toWorld) - 5
                ).map(
                    y -> McHelper.getMaxContentYExclusive(toWorld) - y
                ).mapToObj(y -> new BlockPos(x, y, z)))
            )
            .map(blockPos -> IntBox.getBoxByBasePointAndSize(areaSize, blockPos))
            .filter(intBox -> intBox.stream().allMatch(
                pos -> {
                    BlockState blockState = toWorld.getBlockState(pos);
                    return !blockState.isSolidRender(toWorld, pos) &&
                        blockState.getBlock() != IPRegistry.NETHER_PORTAL_BLOCK.get() &&
                        blockState.getFluidState().isEmpty();
                }
            ))
            .filter(intBox -> intBox.getSurfaceLayer(Direction.DOWN)
                .getMoved(Direction.DOWN.getNormal())
                .stream().allMatch(
                    blockPos -> {
                        BlockState blockState = toWorld.getBlockState(blockPos);
                        return !blockState.isAir() &&
                            blockState.getBlock() != IPRegistry.NETHER_PORTAL_BLOCK.get();
                    }
                )
            )
            .findFirst().orElseGet(() -> IntBox.getBoxByBasePointAndSize(areaSize, toPos))
            .getMoved(Direction.DOWN.getNormal());
    }
    
    public static GeneralBreakablePortal[] createPortals(
        ServerLevel fromWorld, ServerLevel toWorld,
        BlockPortalShape fromShape, BlockPortalShape toShape
    ) {
        GeneralBreakablePortal pa = GeneralBreakablePortal.entityType.create(fromWorld);
        fromShape.initPortalPosAxisShape(pa, true);
        
        pa.setDestination(toShape.innerAreaBox.getCenterVec());
        pa.dimensionTo = toWorld.dimension();
        pa.rotation = new Quaternion(
            new Vector3f(1, 0, 0),
            180,
            true
        );
        
        GeneralBreakablePortal pb = (GeneralBreakablePortal)
            PortalManipulation.createReversePortal(pa, GeneralBreakablePortal.entityType);
        
        pa.blockPortalShape = fromShape;
        pb.blockPortalShape = toShape;
        pa.reversePortalId = pb.getUUID();
        pb.reversePortalId = pa.getUUID();
        
        PortalExtension.get(pa).motionAffinity = 0.1;
        PortalExtension.get(pb).motionAffinity = 0.1;
        
        McHelper.spawnServerEntity(pa);
        McHelper.spawnServerEntity(pb);
        
        return new GeneralBreakablePortal[]{pa, pb};
    }
    
}
