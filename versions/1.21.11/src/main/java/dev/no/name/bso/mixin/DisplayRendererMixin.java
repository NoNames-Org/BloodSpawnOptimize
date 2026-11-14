package dev.no.name.bso.mixin;

import dev.no.name.bso.DisplayCulling;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.state.DisplayEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DisplayRenderer.class)
public abstract class DisplayRendererMixin {
    @Inject(method = "getShadowRadius(Lnet/minecraft/client/renderer/entity/state/DisplayEntityRenderState;)F",
            at = @At("HEAD"), cancellable = true)
    private void bso$noShadow(DisplayEntityRenderState state, CallbackInfoReturnable<Float> callback) {
        if (DisplayCulling.hideShadows()) {
            callback.setReturnValue(0f);
        }
    }
}
