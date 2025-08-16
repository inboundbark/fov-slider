package io.github.inboundbark.fovslider;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

import static io.github.inboundbark.fovslider.FOVSlider.*;

public class FOVConfig {
    @ConfigEntry(name = "Field of View (FOV)", minValue = FOV_MIN, maxValue = FOV_MAX)
    public Integer FOV = FOV_DEFAULT;

    @ConfigEntry(name = "View Model FOV", description = "Like in Source games.", minValue = FOV_MIN, maxValue = FOV_MAX)
    public Integer viewModelFOV = VIEW_MODEL_FOV_DEFAULT;

    @ConfigEntry(name = "Enable FOV Slider", description = "Disabling may prevent conflicts with other options menu mods.", requiresRestart = true)
    public Boolean FOVSlider = true;
}
