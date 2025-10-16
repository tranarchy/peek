package peek.util;

import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import peek.Main;

public class PeekScreen extends GenericContainerScreen {
    public PeekScreen(Text name, Inventory inventory) {
        super(GenericContainerScreenHandler.createGeneric9x3(-1, Main.mc.player.getInventory(), inventory), Main.mc.player.getInventory(), name);
    }
}
