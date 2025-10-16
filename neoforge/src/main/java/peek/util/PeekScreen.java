package peek.util;

import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;
import peek.Main;

public class PeekScreen extends ContainerScreen {
    public PeekScreen(Component name, Container inventory) {
        super(ChestMenu.threeRows(-1, Main.mc.player.getInventory(), inventory), Main.mc.player.getInventory(), name);
    }
}
