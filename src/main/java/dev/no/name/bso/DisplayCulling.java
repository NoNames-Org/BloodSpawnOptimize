package dev.no.name.bso;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Display;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class DisplayCulling {
    private static final double HALF_DIAGONAL = 0.87;

    private DisplayCulling() {
    }

    public static Boolean shouldRender(Display display, Frustum view, double camX, double camY, double camZ, float partialTick) {
        BsoConfig config = BsoConfig.get();
        if (!config.enabled || !handles(display, config)) {
            return null;
        }
        double max = config.distance;
        if (display.distanceToSqr(camX, camY, camZ) > max * max) {
            return false;
        }
        if (!config.frustum) {
            return null;
        }
        Display.RenderState state = display.renderState();
        if (state == null) {
            return null;
        }
        return view.isVisible(cullingBox(display, state.transformation().get(partialTick), config.margin));
    }

    public static boolean hideShadows() {
        BsoConfig config = BsoConfig.get();
        return config.enabled && config.hideShadows;
    }

    private static boolean handles(Display display, BsoConfig config) {
        return display instanceof Display.BlockDisplay || display instanceof Display.ItemDisplay
                || display instanceof Display.TextDisplay && config.textDisplays;
    }

    private static AABB cullingBox(Display display, Transformation transformation, double margin) {
        Matrix4fc matrix = transformation.getMatrix();
        Vector3f translation = matrix.getTranslation(new Vector3f());
        Vector3f scale = matrix.getScale(new Vector3f());
        double size = Math.max(Math.abs(scale.x()), Math.max(Math.abs(scale.y()), Math.abs(scale.z())));
        double radius = translation.length() + size * HALF_DIAGONAL + margin;
        return new AABB(display.getX() - radius, display.getY() - radius, display.getZ() - radius,
                display.getX() + radius, display.getY() + radius, display.getZ() + radius);
    }
}
