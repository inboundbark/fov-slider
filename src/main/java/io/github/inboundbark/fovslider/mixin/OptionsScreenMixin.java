package io.github.inboundbark.fovslider.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.option.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;

import static io.github.inboundbark.fovslider.FOVSlider.*;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    @Shadow @Mutable
    private static Option[] RENDER_OPTIONS;

    static {
        if (FOV_CONFIG.FOVSlider) {
            RENDER_OPTIONS = Arrays.copyOf(RENDER_OPTIONS, RENDER_OPTIONS.length + 1);
            RENDER_OPTIONS[RENDER_OPTIONS.length - 1] = FOV_SLIDER;
        }
    }
}
