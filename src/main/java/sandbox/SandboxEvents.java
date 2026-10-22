package sandbox;

import java.util.Optional;

import genesis.api.magic.MagicConcentration;
import genesis.api.planet.PlanetDefinition;
import genesis.mc.GenesisApi;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * The probe reads the world of Genesis while the game is running.
 *
 * <p>Everything it uses comes from {@link GenesisApi}: the planet a dimension is on and the
 * magic of the ground under the player. The probe does not know how the magic is stored, how it
 * is computed or where the planet of a dimension comes from, and it does not have to.
 */
public final class SandboxEvents {

    /** How often the concentration is shown, in ticks. */
    private static final int PERIOD = 40;

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        ServerLevel level = event.getServer().overworld();
        Optional<PlanetDefinition> planet = GenesisApi.planetOf(level);
        Optional<MagicConcentration> concentration = GenesisApi.concentration(level, 0.0, 0.0);
        if (planet.isEmpty() || concentration.isEmpty()) {
            // The overworld is a vanilla one: Genesis is loaded, but this world is not a planet.
            SandboxMod.LOGGER.info("Sandbox: the overworld is not a Genesis world");
            return;
        }
        SandboxMod.LOGGER.info("Sandbox: planet={} concentration={} ({})",
                planet.get().displayName(),
                String.format("%.3f", concentration.get().value()),
                concentration.get().tier());
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (player.tickCount % PERIOD != 0) {
            return;
        }
        ServerLevel level = (ServerLevel) player.level();
        Optional<MagicConcentration> concentration = GenesisApi.concentration(level, player.getX(), player.getZ());
        if (concentration.isEmpty()) {
            return;
        }
        MagicConcentration magic = concentration.get();
        player.sendSystemMessage(Component.literal(String.format(
                "Magic concentration %.3f (%s)", magic.value(), magic.tier())), true);
    }
}
