package peek.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import peek.events.DrawMouseOverToolTipEvent;
import peek.util.EventBus;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Shadow
    protected Slot focusedSlot;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawMouseoverTooltip(Lnet/minecraft/client/gui/DrawContext;II)V", cancellable = true)
    protected void drawMouseoverTooltip(DrawContext drawContext, int x, int y, CallbackInfo info) {
        boolean result = EventBus.post(new DrawMouseOverToolTipEvent(drawContext, x, y, focusedSlot));

        if (!result)
            info.cancel();
    }
}
