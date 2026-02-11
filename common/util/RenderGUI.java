package peek.util;

import net.minecraft.client.gui.GuiGraphics;

public class RenderGUI {
    public static void drawBorder(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        drawBorderVertical(guiGraphics, x, y, width, height, color);
        drawBorderHorizontal(guiGraphics, x, y, width, height, color);
    }

    public static void drawBorderVertical(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        guiGraphics.vLine(x, y , y + height, color);
        guiGraphics.vLine(x + width, y, y + height, color);
    }

    public static void drawBorderHorizontal(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        guiGraphics.hLine(x, x + width, y, color);
        guiGraphics.hLine(x, x + width, y + height, color);
    }
}
