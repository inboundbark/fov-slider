package io.github.inboundbark.fovslider.mixin;

import net.minecraft.client.option.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Option.class)
public interface OptionInvoker {
    // Yes, this is duplicated code from StAPI.
    // I'd rather have this than needing an entire dependency for only one method.
    @Invoker("<init>")
    static Option fovslider_invokeInitOption(String optionEnumName, int enumOrdinal, String key, boolean slider, boolean toggle) {
        throw new AssertionError();
    }
}
