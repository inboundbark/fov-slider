package io.github.inboundbark.fovslider;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

import static io.github.inboundbark.fovslider.FOVSlider.FOV_MAX;
import static io.github.inboundbark.fovslider.FOVSlider.FOV_MIN;

public class FOVConfig {
    @ConfigEntry(name = "Field of View (FOV)", minValue = FOV_MIN, maxValue = FOV_MAX)
    public Integer FOV = 70;

    @ConfigEntry(name = "View Model FOV", description = "Like in Source games.", minValue = FOV_MIN, maxValue = FOV_MAX)
    public Integer viewModelFOV = 70;

    @ConfigEntry(name = "Enable FOV Slider", description = "Disabling may prevent conflicts with other options menu mods.", requiresRestart = true)
    public Boolean FOVSlider = true;
}
