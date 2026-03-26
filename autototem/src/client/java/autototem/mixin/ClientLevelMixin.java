package autototem.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Unique
    private final Minecraft mc = Minecraft.getInstance();

    @Unique
    private void move(int slot1, int slot2) {
        int syncId = mc.player.containerMenu.containerId;

        mc.gameMode.handleContainerInput(syncId, slot1, 0, ContainerInput.PICKUP, mc.player);
        mc.gameMode.handleContainerInput(syncId, slot2, 0, ContainerInput.PICKUP, mc.player);
    }

    @Unique
    private boolean isContainerOpen() {
        if (mc.screen instanceof ContainerScreen || mc.screen instanceof ShulkerBoxScreen)
            return true;

        return false;
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        if (!mc.player.getOffhandItem().isEmpty() || isContainerOpen())
            return;

        for (int i = 0; i <= 35; i++) {
            if (mc.player.getInventory().getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
                move(i < 9 ? 36 + i : i, 45);
                break;
            }
        }
    }
}
