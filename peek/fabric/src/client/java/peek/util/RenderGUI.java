package peek.util;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public class RenderGUI {
    public static void drawBorder(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int color) {
        drawBorderVertical(guiGraphics, x, y, width, height, color);
        drawBorderHorizontal(guiGraphics, x, y, width, height, color);
    }

    public static void drawBorderVertical(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int color) {
        guiGraphics.verticalLine(x, y , y + height, color);
        guiGraphics.verticalLine(x + width, y, y + height, color);
    }

    public static void drawBorderHorizontal(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int color) {
        guiGraphics.horizontalLine(x, x + width, y, color);
        guiGraphics.horizontalLine(x, x + width, y + height, color);
    }
}
