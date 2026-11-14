package dev.no.name.bso.mixin;

import dev.no.name.bso.DisplayCulling;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void bso$cullDisplays(Entity entity, Frustum frustum, double camX, double camY, double camZ,
                                  float partialTick, CallbackInfoReturnable<Boolean> callback) {
        if (entity instanceof Display display) {
            Boolean verdict = DisplayCulling.shouldRender(display, frustum, camX, camY, camZ, partialTick);
            if (verdict != null) {
                callback.setReturnValue(verdict);
            }
        }
    }
}
