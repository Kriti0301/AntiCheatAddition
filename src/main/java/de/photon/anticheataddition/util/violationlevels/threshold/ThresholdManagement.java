package de.photon.anticheataddition.util.violationlevels.threshold;

import com.google.common.base.Preconditions;
import de.photon.anticheataddition.AntiCheatAddition;
import de.photon.anticheataddition.modules.ViolationModule;
import de.photon.anticheataddition.util.config.ConfigUtils;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public interface ThresholdManagement
{
    /**
     * Empty {@link ThresholdManagement} that doesn't have any {@link Threshold}s.
     */
    ThresholdManagement EMPTY = (fromVl, toVl, player) -> {};

    @NotNull
    static ThresholdManagement loadThresholds(ViolationModule module)
    {
        return loadThresholds(module.getConfigString() + ".thresholds");
    }

    @NotNull
    static ThresholdManagement loadThresholds(String configPath)
    {
        Preconditions.checkNotNull(configPath, "Tried to load null config path.");
        val keys = Preconditions.checkNotNull(ConfigUtils.loadKeys(configPath), "Config loading error: The keys loaded from " + configPath + " are null.");
        val thresholds = keys.stream().map(key -> new Threshold(Integer.parseInt(key), ConfigUtils.loadImmutableStringOrStringList(configPath + '.' + key))).collect(Collectors.toList());

        switch (thresholds.size()) {
            case 0: return EMPTY;
            case 1: return new SingleThresholds(thresholds.get(0));
            default: return new MultiThresholds(thresholds);
        }
    }

    @NotNull
    static ThresholdManagement loadCommands(String configPath)
    {
        Preconditions.checkNotNull(configPath, "Tried to load null config path.");
        Preconditions.checkArgument(AntiCheatAddition.getInstance().getConfig().contains(configPath), "Config loading error: The commands at " + configPath + " could not be loaded or the path does not exist.");
        val commands = ConfigUtils.loadImmutableStringOrStringList(configPath);
        return commands.isEmpty() ? EMPTY : new SingleThresholds(new Threshold(1, commands));
    }

    /**
     * Used to execute the commands of the {@link Threshold}s in this  {@link MultiThresholds}.
     */
    void executeThresholds(int fromVl, int toVl, @NotNull Player players);
}
