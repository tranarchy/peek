package peek.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import peek.events.RenderTooltipEvent;
import peek.util.EventBus;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Shadow
    protected net.minecraft.world.inventory.Slot hoveredSlot;

    @Inject(at = @At("HEAD"), method = "renderTooltip", cancellable = true)
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y, CallbackInfo info) {
        boolean result = EventBus.post(new RenderTooltipEvent(guiGraphics, x, y, hoveredSlot));

        if (!result)
            info.cancel();
    }
}
