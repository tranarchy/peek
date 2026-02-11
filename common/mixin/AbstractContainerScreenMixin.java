package peek.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import peek.Main;
import peek.util.PeekScreen;
import peek.util.RenderGUI;

import java.util.List;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Shadow
    protected Slot hoveredSlot;

    @Inject(at = @At("HEAD"), method = "renderTooltip", cancellable = true)
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y, CallbackInfo info) {
        if (hoveredSlot == null)
            return;

        ItemStack focusedStack = hoveredSlot.getItem();

        final int SLOT_WIDTH = 18;
        final int MAX_WIDTH = SLOT_WIDTH * 9;

        final int FOREGROUND_COLOR = 0xFF626282;
        final int BACKGROUND_COLOR = 0xC810101A;
        final int BORDER_COLOR = 0xFF10101A;

        if (focusedStack.is(ItemTags.SHULKER_BOXES) || (focusedStack.getItem() == Items.ENDER_CHEST && Main.echestWasOpened)) {

            Minecraft mc = Minecraft.getInstance();

            List<ItemStack> itemsToPeek;

            if (focusedStack.getItem() == Items.ENDER_CHEST) {
                itemsToPeek = Main.enderChestItems;
            } else {
                ItemContainerContents shulkerContainer = focusedStack.getComponents().get(DataComponents.CONTAINER);
                itemsToPeek = shulkerContainer.stream().toList();
            }

            if (InputConstants.isKeyDown(mc.getWindow(), InputConstants.KEY_LALT)) {
                SimpleContainer peekInventory = new SimpleContainer(9 * 3);

                for (int i = 0; i < itemsToPeek.size(); i++) {
                    peekInventory.setItem(i, itemsToPeek.get(i));
                }

                mc.setScreen(new PeekScreen(focusedStack.getHoverName(), peekInventory));
            }

            int posX = x + (SLOT_WIDTH / 2);
            int posY = y;

            int itemIndex = 0;

            Font textRenderer = mc.font;

            ItemStack shulkerItem;

            Matrix3x2fStack matrix3fStack = guiGraphics.pose().pushMatrix();

            guiGraphics.pose().translate(0.0F, 0.0F, matrix3fStack.identity());

            guiGraphics.fill(posX, posY, posX + MAX_WIDTH, posY + textRenderer.lineHeight + 4, BACKGROUND_COLOR);
            RenderGUI.drawBorder(guiGraphics, posX, posY, MAX_WIDTH, textRenderer.lineHeight + 4, BORDER_COLOR);
            guiGraphics.drawString(textRenderer, focusedStack.getHoverName(), posX + 3, posY + 3, FOREGROUND_COLOR, true);

            posY += textRenderer.lineHeight + 4;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {

                    shulkerItem = itemsToPeek.size() > itemIndex ? itemsToPeek.get(itemIndex) : ItemStack.EMPTY;
                    int stackCount = shulkerItem.getCount();

                    guiGraphics.fill(posX, posY, posX + SLOT_WIDTH, posY + SLOT_WIDTH, BACKGROUND_COLOR);
                    RenderGUI.drawBorder(guiGraphics, posX, posY, SLOT_WIDTH, SLOT_WIDTH, BORDER_COLOR);

                    guiGraphics.renderItem(shulkerItem, posX + 1, posY + 1);
                    guiGraphics.renderItemDecorations(textRenderer, shulkerItem, posX + 1, posY + 1, stackCount > 1 ? String.valueOf(stackCount) : "");

                    posX += SLOT_WIDTH;
                    itemIndex++;
                }

                posX = x + (SLOT_WIDTH / 2);
                posY += SLOT_WIDTH;
            }

            matrix3fStack.popMatrix();

            info.cancel();
        }
    }
}
