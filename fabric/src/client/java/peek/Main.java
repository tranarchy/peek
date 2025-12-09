package peek;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import org.joml.Matrix3x2fStack;
import peek.events.ClientLevelTickEvent;
import peek.events.RenderTooltipEvent;
import peek.util.EventBus;
import peek.util.PeekScreen;
import peek.util.RenderGUI;

import java.util.ArrayList;
import java.util.List;

public class Main implements ClientModInitializer {
	public static boolean echestWasOpened = false;
	public static List<ItemStack> enderChestItems = new ArrayList<>();
	public static Minecraft mc = Minecraft.getInstance();

	@Override
	public void onInitializeClient() {
		final int SLOT_WIDTH = 18;
		final int MAX_WIDTH = SLOT_WIDTH * 9;

		final int FOREGROUND_COLOR = 0xFF626282;
		final int BACKGROUND_COLOR = 0xC810101A;
		final int BORDER_COLOR = 0xFF10101A;

		EventBus.register(RenderTooltipEvent.class, event -> {
			if (event.focusedSlot != null) {
				ItemStack focusedStack = event.focusedSlot.getItem();

				if (focusedStack.is(ItemTags.SHULKER_BOXES) || (focusedStack.getItem() == Items.ENDER_CHEST && echestWasOpened)) {

					List<ItemStack> itemsToPeek;

					if (focusedStack.getItem() == Items.ENDER_CHEST) {
						itemsToPeek = enderChestItems;
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

					int posX = event.x + (SLOT_WIDTH / 2);
					int posY = event.y;

					int itemIndex = 0;

					Font textRenderer = mc.font;

					ItemStack shulkerItem;

					Matrix3x2fStack matrix3fStack = event.drawContext.pose().pushMatrix();

					event.drawContext.pose().translate(0.0F, 0.0F, matrix3fStack.identity());

                    event.drawContext.fill(posX, posY, posX + MAX_WIDTH, posY + textRenderer.lineHeight + 4, BACKGROUND_COLOR);
                    RenderGUI.drawBorder(event.drawContext, posX, posY, MAX_WIDTH, textRenderer.lineHeight + 4, BORDER_COLOR);
                    event.drawContext.drawString(textRenderer, focusedStack.getHoverName(), posX + 3, posY + 3, FOREGROUND_COLOR, true);

                    posY += textRenderer.lineHeight + 4;

					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 9; j++) {

							shulkerItem = itemsToPeek.size() > itemIndex ? itemsToPeek.get(itemIndex) : ItemStack.EMPTY;
							int stackCount = shulkerItem.getCount();

							event.drawContext.fill(posX, posY, posX + SLOT_WIDTH, posY + SLOT_WIDTH, BACKGROUND_COLOR);
							RenderGUI.drawBorder(event.drawContext, posX, posY, SLOT_WIDTH, SLOT_WIDTH, BORDER_COLOR);

							event.drawContext.renderItem(shulkerItem, posX + 1, posY + 1);
							event.drawContext.renderItemDecorations(textRenderer, shulkerItem, posX + 1, posY + 1, stackCount > 1 ? String.valueOf(stackCount) : "");

							posX += SLOT_WIDTH;
							itemIndex++;
						}

						posX = event.x + (SLOT_WIDTH / 2);
						posY += SLOT_WIDTH;
					}

					matrix3fStack.popMatrix();

					return false;
				}
			}

			return true;
		});

		EventBus.register(ClientLevelTickEvent.class, event -> {
            if (mc.screen instanceof ContainerScreen genericContainerScreen) {
                if (genericContainerScreen.getTitle().toString().contains("enderchest")) {
                    echestWasOpened = true;
                    enderChestItems.clear();
                    genericContainerScreen.getMenu().slots.forEach(slot -> {
                        if (enderChestItems.size() < 27)
                            enderChestItems.add(slot.getItem());
                    });
                }
            }

			return true;
		});
	}
}
