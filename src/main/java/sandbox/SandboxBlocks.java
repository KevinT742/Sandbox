package sandbox;

import genesis.api.item.ItemCategory;
import genesis.api.item.ItemRarity;
import genesis.api.item.ItemTemplate;
import genesis.api.magic.MagicInput;
import genesis.api.material.BlockTemplate;
import genesis.api.material.MaterialFamily;
import genesis.api.material.MaterialRegistry;
import genesis.mc.GenesisApi;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * The blocks and items of the probe.
 *
 * <p>Nothing here says how a block behaves. The block is described by a
 * {@link BlockTemplate}, the template carries a material profile, and the material profile
 * carries the physics, the chemistry and the magic. Genesis turns that into the properties of
 * Minecraft, so the probe only has to say <em>what</em> the block is made of.
 */
public final class SandboxBlocks {

    /** The registry that holds the blocks of the probe. */
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SandboxMod.MODID);
    /** The registry that holds the items of the probe. */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SandboxMod.MODID);

    /** A block of ordinary stone, described the way Genesis describes its own blocks. */
    public static final BlockTemplate PROBE_BLOCK = BlockTemplate.builder("probe_block")
            .profile(MaterialFamily.rock("sandbox:probe_block"))
            .toolTier(BlockTemplate.ToolTier.STONE)
            .sound(BlockTemplate.SoundKind.STONE)
            .build();

    public static final DeferredBlock<Block> PROBE_BLOCK_REG =
            BLOCKS.registerSimpleBlock("probe_block", GenesisApi.blockProperties(PROBE_BLOCK));

    public static final DeferredItem<BlockItem> PROBE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("probe_block", PROBE_BLOCK_REG);

    /** A shard of magic crystal: the magic model of Genesis decides that it is a magic material. */
    public static final ItemTemplate PROBE_SHARD = ItemTemplate.builder("probe_shard")
            .profile(MaterialFamily.magicMaterial("sandbox:probe_shard", 0.0002))
            .maxStackSize(64)
            .rarity(ItemRarity.UNCOMMON)
            .category(ItemCategory.MATERIAL)
            .magicInput(MagicInput.MATERIAL)
            .build();

    public static final DeferredItem<Item> PROBE_SHARD_ITEM =
            ITEMS.registerSimpleItem("probe_shard", GenesisApi.itemProperties(PROBE_SHARD));

    private SandboxBlocks() {
    }

    /** Registers the materials of the probe with the registry of Genesis. */
    public static void registerProfiles() {
        MaterialRegistry.register(PROBE_BLOCK.profile());
        MaterialRegistry.register(PROBE_SHARD.profile());
    }
}
