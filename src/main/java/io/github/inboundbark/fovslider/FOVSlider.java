package io.github.inboundbark.fovslider;


import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.minecraft.client.option.Option;
import net.modificationstation.stationapi.mixin.keybinding.client.OptionAccessor;

public class FOVSlider {
    @ConfigRoot(value = "config", visibleName = "FOV Slider Config")
    public static final FOVConfig FOV_CONFIG = new FOVConfig();

    public static final Option FOV_SLIDER = OptionAccessor.stationapi_create("FOV", 99, "option.fovslider.fov", true, false);
    public static final int FOV_MIN = 30;
    public static final int FOV_MAX = 110;
    public static final int FOV_RANGE = FOV_MAX - FOV_MIN;
    public static final int FOV_DEFAULT = 70;
    public static final int VIEW_MODEL_FOV_DEFAULT = 70;

    public static float FOVAsFloat(int fov) {
        return (((float) fov) - FOV_MIN) / FOV_RANGE;
    }

    public static int floatAsFOV(float f) { return (int) (f * FOV_RANGE + FOV_MIN); }
}
