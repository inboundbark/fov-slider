package io.github.inboundbark.fovslider.mixin;

import net.glasslauncher.mods.gcapi3.api.GCAPI;
import net.glasslauncher.mods.gcapi3.impl.GlassYamlFile;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static io.github.inboundbark.fovslider.FOVSlider.*;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Unique
    GlassYamlFile yamlOverride = new GlassYamlFile();
    @Unique
    boolean FOVChanged = false;


    @Inject(method = "getFloat", at = @At("HEAD"), cancellable = true)
    void setupGetFOVFloat(Option par1, CallbackInfoReturnable<Float> cir) {
        if (par1 == FOV_SLIDER) {
            cir.setReturnValue(FOVAsFloat(FOV_CONFIG.FOV));
        }
    }

    @Inject(method = "setFloat", at = @At("TAIL"))
    void setupSetFOVFloat(Option value, float par2, CallbackInfo ci) {
        if (value == FOV_SLIDER) {
            FOVChanged = par2 != FOV_CONFIG.FOV; // prevents unnecessary config reloads
            FOV_CONFIG.FOV = floatAsFOV(par2);
        }
    }

    @Inject(method = "save", at = @At(value = "NEW", target = "(Ljava/io/Writer;)Ljava/io/PrintWriter;"))
    void saveFOV(CallbackInfo ci) {
        if (FOVChanged) {
            yamlOverride.set("FOV", FOV_CONFIG.FOV);
            GCAPI.reloadConfig("fovslider:config", yamlOverride);
            FOVChanged = false;
        }
    }

    @Inject(method = "getString", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/option/GameOptions;getFloat(Lnet/minecraft/client/option/Option;)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    void setupFOVString(Option option, CallbackInfoReturnable<String> cir, TranslationStorage translationStorage, String string, float f) {
        if (option == FOV_SLIDER) {
            String fovString = string;
            int fov = floatAsFOV(f);
            if (fov == FOV_DEFAULT) {
                fovString += translationStorage.get("option.fovslider.fov.default");
            }
            else if (fov == FOV_MAX) {
                fovString += translationStorage.get("option.fovslider.fov.max");
            }
            else {
                fovString += fov;
            }
            cir.setReturnValue(fovString);
        }
    }
}
