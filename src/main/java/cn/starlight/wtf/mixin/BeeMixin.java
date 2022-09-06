package cn.starlight.wtf.mixin;

import cn.starlight.wtf.Logger;
import cn.starlight.wtf.Main;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeeEntity.class)
public abstract class BeeMixin {

    @Shadow
    BlockPos hivePos;

    @Inject(at = @At("HEAD"), method = "isHiveValid", cancellable = true)
    private void isHiveValidMixin(CallbackInfoReturnable<Boolean> cir){
        if(!((BeeEntity)(Object)this).hasHive()){
            cir.setReturnValue(false);
        }
        else {
            if(Main.CONF_INSTANCE.isEnable()){
                // 超出设定的最大距离后，直接将该巢穴判定为无效，避免卡服
                double square_distance = calcSquareHorizontalDistance(this.hivePos, ((BeeEntity)(Object)this).getPos());
                double max_distance = Main.CONF_INSTANCE.getMaxDistance();
                if(square_distance > (max_distance * max_distance)){
                    if(Main.CONF_INSTANCE.isDebug()){
                        Logger.warn("存在超出距离的蜜蜂，已将其判定为无效！，其当前坐标为：" + (((BeeEntity)(Object)this).getPos()) + "，其巢穴坐标为" + this.hivePos + "，当前离其巢穴距离为：" + Math.sqrt(square_distance));
                    }
                    cir.setReturnValue(false);
                }
            }
        }
    }

    private double calcSquareHorizontalDistance(BlockPos hivePos, Vec3d beePos){
        double x_distance = hivePos.getX() - beePos.getX();
        double z_distance = hivePos.getZ() - beePos.getZ();
        return x_distance * x_distance + z_distance * z_distance;
    }
}
