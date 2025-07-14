package peek;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.joml.Matrix3x2fStack;
import peek.events.ClientWorldTickEvent;
import peek.events.DrawMouseOverToolTipEvent;
import peek.util.EventBus;
import peek.util.PeekScreen;
import peek.util.RenderGUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main implements ClientModInitializer {
	public static boolean echestWasOpened = false;
	public static List<ItemStack> enderChestItems = new ArrayList<>();
	public static MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {
		final List<Item> shulkerBoxItems = Arrays.asList(
				Items.SHULKER_BOX,
				Items.WHITE_SHULKER_BOX,
				Items.ORANGE_SHULKER_BOX,
				Items.MAGENTA_SHULKER_BOX,
				Items.LIGHT_BLUE_SHULKER_BOX,
				Items.YELLOW_SHULKER_BOX,
				Items.LIME_SHULKER_BOX,
				Items.PINK_SHULKER_BOX,
				Items.GRAY_SHULKER_BOX,
				Items.LIGHT_GRAY_SHULKER_BOX,
				Items.CYAN_SHULKER_BOX,
				Items.PURPLE_SHULKER_BOX,
				Items.BLUE_SHULKER_BOX,
				Items.BROWN_SHULKER_BOX,
				Items.GREEN_SHULKER_BOX,
				Items.RED_SHULKER_BOX,
				Items.BLACK_SHULKER_BOX
		);

		final int SLOT_WIDTH = 18;
		final int MAX_WIDTH = SLOT_WIDTH * 9;

		final int FOREGROUND_COLOR = 0xFF626282;
		final int BACKGROUND_COLOR = 0xC810101A;
		final int BORDER_COLOR = 0xFF10101A;

		EventBus.register(DrawMouseOverToolTipEvent.class, event -> {
			if (event.focusedSlot != null) {
				ItemStack focusedStack = event.focusedSlot.getStack();

				if (shulkerBoxItems.contains(focusedStack.getItem()) || (focusedStack.getItem() == Items.ENDER_CHEST && echestWasOpened)) {

					List<ItemStack> itemsToPeek;

					if (focusedStack.getItem() == Items.ENDER_CHEST) {
						itemsToPeek = enderChestItems;
					} else {
						ContainerComponent shulkerContainer = focusedStack.getComponents().get(DataComponentTypes.CONTAINER);
						itemsToPeek = shulkerContainer.stream().toList();
					}

					if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_ALT)) {
						SimpleInventory peekInventory = new SimpleInventory(9 * 3);

						for (int i = 0; i < itemsToPeek.size(); i++) {
							peekInventory.setStack(i, itemsToPeek.get(i));
						}
						mc.setScreen(new PeekScreen(focusedStack.getName(), peekInventory));
					}

					int posX = event.x + (SLOT_WIDTH / 2);
					int posY = event.y;

					int itemIndex = 0;

					TextRenderer textRenderer = mc.textRenderer;

					ItemStack shulkerItem;

					Matrix3x2fStack matrix3fStack = event.drawContext.getMatrices().pushMatrix();

					event.drawContext.getMatrices().translate(0.0F, 0.0F, matrix3fStack.identity());

					event.drawContext.fill(posX, posY, posX + MAX_WIDTH, posY + textRenderer.fontHeight + 4, BACKGROUND_COLOR);
					RenderGUI.drawBorder(event.drawContext, posX, posY, MAX_WIDTH, textRenderer.fontHeight + 4, BORDER_COLOR);
					event.drawContext.drawText(textRenderer, focusedStack.getName(), posX + 3, posY + 3, FOREGROUND_COLOR, true);

					posY += textRenderer.fontHeight + 4;

					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 9; j++) {

							shulkerItem = itemsToPeek.size() > itemIndex ? itemsToPeek.get(itemIndex) : ItemStack.EMPTY;
							int stackCount = shulkerItem.getCount();

							event.drawContext.fill(posX, posY, posX + SLOT_WIDTH, posY + SLOT_WIDTH, BACKGROUND_COLOR);
							RenderGUI.drawBorder(event.drawContext, posX, posY, SLOT_WIDTH, SLOT_WIDTH, BORDER_COLOR);

							event.drawContext.drawItem(shulkerItem, posX + 1, posY + 1);
							event.drawContext.drawStackOverlay(textRenderer, shulkerItem, posX + 1, posY + 1, stackCount > 1 ? String.valueOf(stackCount) : "");

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

		EventBus.register(ClientWorldTickEvent.class, event -> {
			if (mc.currentScreen instanceof GenericContainerScreen genericContainerScreen) {
				if (genericContainerScreen.getTitle().toString().contains("enderchest")) {
					echestWasOpened = true;
					enderChestItems.clear();
					genericContainerScreen.getScreenHandler().slots.forEach(slot -> {
						if (enderChestItems.size() < 27)
							enderChestItems.add(slot.getStack());
					});
				}
			}

			return true;
		});
	}
}