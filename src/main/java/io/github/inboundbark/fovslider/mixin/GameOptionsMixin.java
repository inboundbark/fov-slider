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
    boolean needReloadConfig = false;


    @Inject(method = "getFloat", at = @At("HEAD"), cancellable = true)
    void fovslider_setupGetFOVFloat(Option option, CallbackInfoReturnable<Float> cir) {
        if (option == FOV_SLIDER) {
            cir.setReturnValue(FOVAsFloat(FOV_CONFIG.FOV));
        }
    }

    @Inject(method = "setFloat", at = @At("TAIL"))
    void fovslider_setupSetFOVFloat(Option option, float value, CallbackInfo ci) {
        if (option == FOV_SLIDER) {
            FOV_CONFIG.FOV = floatAsFOV(value);
            needReloadConfig = true;
        }
    }

    @Inject(method = "save", at = @At(value = "NEW", target = "(Ljava/io/Writer;)Ljava/io/PrintWriter;"))
    void fovslider_saveFOV(CallbackInfo ci) {
        if (needReloadConfig) {
            yamlOverride.set("FOV", FOV_CONFIG.FOV);
            GCAPI.reloadConfig("fovslider:config", yamlOverride);
            needReloadConfig = false;
        }
    }

    @Inject(method = "getString", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/option/GameOptions;getFloat(Lnet/minecraft/client/option/Option;)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    void fovslider_setupFOVString(Option option, CallbackInfoReturnable<String> cir, TranslationStorage translationStorage, String string, float f) {
        if (option == FOV_SLIDER) {
            int fov = floatAsFOV(f);
            cir.setReturnValue(string + switch (fov) {
                case FOV_DEFAULT -> translationStorage.get("option.fovslider.fov.default");
                case FOV_MAX -> translationStorage.get("option.fovslider.fov.max");
                default -> fov;
            });
        }
    }
}
