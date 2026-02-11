package peek;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Main implements ClientModInitializer {
	public static boolean echestWasOpened = false;
	public static List<ItemStack> enderChestItems = new ArrayList<>();
	public static Minecraft mc = Minecraft.getInstance();

	@Override
	public void onInitializeClient() {}
}
