package sandbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * The entry point of the probe.
 *
 * <p>The probe is a member of the mod family in every sense: it is its own repository, it is
 * built by the family root, and it gets everything it needs from Genesis through
 * {@code implementation project(':base')}.
 *
 * <p>It deliberately does almost nothing. Its job is to prove that the way into Genesis works
 * and to be the template every further member can be copied from.
 */
@Mod(SandboxMod.MODID)
public class SandboxMod {

    /** The unique identifier of the mod. */
    public static final String MODID = "sandbox";

    /** The log of the probe. */
    public static final Logger LOGGER = LoggerFactory.getLogger("Sandbox");

    public SandboxMod(IEventBus modEventBus, ModContainer modContainer) {
        SandboxBlocks.BLOCKS.register(modEventBus);
        SandboxBlocks.ITEMS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        // The events of the game live on the NeoForge bus, the events of the mod on its own bus.
        NeoForge.EVENT_BUS.register(new SandboxEvents());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // The material of the probe joins the registry of Genesis, so that a block of the probe
        // has a material just like a block of Genesis.
        SandboxBlocks.registerProfiles();
    }
}
