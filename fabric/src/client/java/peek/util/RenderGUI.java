package peek.util;

import net.minecraft.client.gui.GuiGraphics;

public class RenderGUI {
    public static void drawBorder(GuiGraphics drawContext, int x, int y, int width, int height, int color) {
        drawBorderVertical(drawContext, x, y, width, height, color);
        drawBorderHorizontal(drawContext, x, y, width, height, color);
    }

    public static void drawBorderVertical(GuiGraphics drawContext, int x, int y, int width, int height, int color) {
        drawContext.vLine(x, y , y + height, color);
        drawContext.vLine(x + width, y, y + height, color);
    }

    public static void drawBorderHorizontal(GuiGraphics drawContext, int x, int y, int width, int height, int color) {
        drawContext.hLine(x, x + width, y, color);
        drawContext.hLine(x, x + width, y + height, color);
    }
}
