package sandbox.dev;

import genesis.api.item.ItemCategory;
import genesis.api.item.ItemRarity;
import genesis.api.item.ItemTemplate;
import genesis.api.magic.MagicInput;
import genesis.api.material.BlockTemplate;
import genesis.api.material.MaterialFamily;
import genesis.api.material.MaterialProfile;
import genesis.api.material.MaterialRegistry;

/**
 * The self test of the probe.
 *
 * <p>It proves the thing the probe is for: the contracts of Genesis can be used by a member of
 * the family <em>without reaching into its internals</em>. A third party describes its blocks
 * with a template, and the rules of Genesis decide what those blocks are like.
 *
 * <p>It deliberately builds its own templates instead of using the ones of the mod, so that the
 * test is about the contract and not about the code that happens to use it.
 */
public final class SandboxSelfTest {

    private static int checks;
    private static int failures;

    private SandboxSelfTest() {
    }

    public static void main(String[] args) {
        System.out.println("== The contracts of Genesis, seen from a member of the family ==");
        testTemplates();
        testMaterials();

        System.out.println("checks  : " + checks);
        System.out.println("failures: " + failures);
        if (failures > 0) {
            System.out.println("SANDBOX SELF TEST FAILED");
            System.exit(1);
        }
        System.out.println("SANDBOX SELF TEST PASSED");
    }

    private static void testTemplates() {
        BlockTemplate block = BlockTemplate.builder("probe_block")
                .profile(MaterialFamily.rock("sandbox:probe_block"))
                .toolTier(BlockTemplate.ToolTier.STONE)
                .sound(BlockTemplate.SoundKind.STONE)
                .build();

        check("a third party can describe a block with the template of Genesis", block != null);
        check("the template carries the name of the block", "probe_block".equals(block.name()));
        check("the template keeps the tool tier it was given",
                block.toolTier() == BlockTemplate.ToolTier.STONE);
        check("the template keeps the sound it was given",
                block.sound() == BlockTemplate.SoundKind.STONE);
        check("the template of a third party has a material", block.profile() != null);
        check("the material of a third party has a hardness", block.miningHardness() > 0.0);
    }

    private static void testMaterials() {
        ItemTemplate shard = ItemTemplate.builder("probe_shard")
                .profile(MaterialFamily.magicMaterial("sandbox:probe_shard", 0.0002))
                .maxStackSize(64)
                .rarity(ItemRarity.UNCOMMON)
                .category(ItemCategory.MATERIAL)
                .magicInput(MagicInput.MATERIAL)
                .build();

        check("the magic model of Genesis calls the item a magic material", shard.isMagicMaterial());
        check("the magic model of Genesis calls the item brewable", shard.isBrewable());
        check("the item has the mass the material gives it", shard.mass() > 0.0);
        check("the item has the volume the material gives it", shard.volume() > 0.0);
        check("the item has the density the material gives it", shard.density() > 0.0);

        MaterialProfile profile = shard.profile();
        MaterialRegistry.register(profile);
        check("the material of a third party lands in the registry of Genesis",
                MaterialRegistry.get("sandbox:probe_shard").isPresent());
    }

    private static void check(String name, boolean condition) {
        checks++;
        if (condition) {
            System.out.println("  [ok]   " + name);
        } else {
            failures++;
            System.out.println("  [FAIL] " + name);
        }
    }
}
