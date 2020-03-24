package de.photon.aacadditionpro.modules.checks.inventory;

import de.photon.aacadditionpro.modules.ModuleType;
import de.photon.aacadditionpro.modules.PatternModule;
import de.photon.aacadditionpro.olduser.UserOld;
import de.photon.aacadditionpro.olduser.data.PositionDataOld;
import de.photon.aacadditionpro.util.entity.EntityUtil;
import de.photon.aacadditionpro.util.files.configs.LoadFromConfiguration;
import de.photon.aacadditionpro.util.inventory.InventoryUtils;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;

class SprintingPattern extends PatternModule.Pattern<UserOld, InventoryClickEvent>
{
    @LoadFromConfiguration(configPath = ".cancel_vl")
    @Getter
    private int cancelVl;

    @Override
    protected int process(UserOld user, InventoryClickEvent event)
    {
        // Flight may trigger this
        if (!user.getPlayer().getAllowFlight() &&
            // Not using an Elytra
            !EntityUtil.isFlyingWithElytra(user.getPlayer()) &&
            // Sprinting and Sneaking as detection
            (user.getPlayer().isSprinting() || user.getPlayer().isSneaking()) &&
            // The player opened the inventory at least a quarter second ago
            user.getInventoryData().notRecentlyOpened(250) &&
            // Is the player moving
            user.getPositionData().hasPlayerMovedRecently(1000, PositionDataOld.MovementType.ANY))
        {
            message = "Inventory-Verbose | Player: " + user.getPlayer().getName() + " interacted with an inventory while sprinting or sneaking.";
            return 20;
        }
        return 0;
    }

    @Override
    public void cancelAction(UserOld user, InventoryClickEvent event)
    {
        event.setCancelled(true);
        InventoryUtils.syncUpdateInventory(user.getPlayer());
    }

    @Override
    public String getConfigString()
    {
        return this.getModuleType().getConfigString() + ".parts.Sprinting";
    }

    @Override
    public ModuleType getModuleType()
    {
        return ModuleType.INVENTORY;
    }
}
