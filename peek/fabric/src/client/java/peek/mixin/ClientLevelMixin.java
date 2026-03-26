package peek.mixin;

import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import peek.Main;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        if (Main.mc.screen instanceof ContainerScreen genericContainerScreen) {
            if (genericContainerScreen.getTitle().toString().contains("enderchest")) {
                Main.echestWasOpened = true;
                Main.enderChestItems.clear();
                genericContainerScreen.getMenu().slots.forEach(slot -> {
                    if (Main.enderChestItems.size() < 27)
                        Main.enderChestItems.add(slot.getItem());
                });
            }
        }
    }
}
