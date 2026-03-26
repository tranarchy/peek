package peek;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;

@Mod(value = "peek", dist = Dist.CLIENT)
@EventBusSubscriber(modid = "peek", value = Dist.CLIENT)
public class Main {
	public static boolean echestWasOpened = false;
	public static List<ItemStack> enderChestItems = new ArrayList<>();
	public static Minecraft mc;

    @SubscribeEvent
	static void onClientSetup(FMLClientSetupEvent e) {
		final int SLOT_WIDTH = 18;
		final int MAX_WIDTH = SLOT_WIDTH * 9;

		final int FOREGROUND_COLOR = 0xFF626282;
		final int BACKGROUND_COLOR = 0xC810101A;
		final int BORDER_COLOR = 0xFF10101A;

        mc = Minecraft.getInstance();
	}
}
