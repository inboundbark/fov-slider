package io.github.inboundbark.fovslider.mixin;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.inboundbark.fovslider.FOVSlider.FOV_CONFIG;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow private Minecraft client;
    @Shadow private float viewDistance;
    @Shadow private float prevCameraRoll;
    @Shadow private float cameraRoll;

    @Unique private static final float UNDERWATER_FOV_MULTIPLIER = 60.0F / 70.0F;

    @Shadow
    private float getFov(float tickDelta) {
        return 0;
    }

    @Unique
    private float getFOVModifier(float tickDelta, boolean isViewModel) {
        LivingEntity var2 = this.client.camera;
        float FOV = (float) FOV_CONFIG.FOV;

        if (isViewModel) {
            FOV = (float) FOV_CONFIG.viewModelFOV;
        }

        if (var2.isInFluid(Material.WATER)) {
            FOV *= UNDERWATER_FOV_MULTIPLIER;
        }

        if (var2.health <= 0) {
            float var4 = (float)var2.deathTime + tickDelta;
            FOV /= (1.0F - 500.0F / (var4 + 500.0F)) * 2.0F + 1.0F;
        }

        return FOV + this.prevCameraRoll + (this.cameraRoll - this.prevCameraRoll) * tickDelta;
    }

    @Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getFov(F)F"))
    private float getFOV(GameRenderer instance, float tickDelta) {
        return this.getFOVModifier(tickDelta, false);
    }

    @Inject(method = "renderFirstPersonHand", at = @At("HEAD"))
    private void setupViewModelFOV(float tickDelta, int eye, CallbackInfo ci) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(this.getFOVModifier(tickDelta, true), (float)this.client.displayWidth / (float)this.client.displayHeight, 0.05F, this.viewDistance * 2.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
