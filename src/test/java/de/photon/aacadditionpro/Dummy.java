package de.photon.aacadditionpro;

import de.photon.aacadditionpro.modules.ViolationModule;
import de.photon.aacadditionpro.user.User;
import de.photon.aacadditionpro.user.data.DataMap;
import de.photon.aacadditionpro.user.data.TimestampMap;
import lombok.val;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mockito.Mockito;

import java.io.File;
import java.util.UUID;

public class Dummy
{
    public static AACAdditionPro mockAACAdditionPro()
    {
        val config = YamlConfiguration.loadConfiguration(new File("src/main/resources/config.yml"));
        AACAdditionPro mockAACAdditionPro = Mockito.mock(AACAdditionPro.class);
        Mockito.when(mockAACAdditionPro.getConfig()).thenReturn(config);
        AACAdditionPro.setInstance(mockAACAdditionPro);
        return mockAACAdditionPro;
    }

    public static Player mockPlayer()
    {
        Player player = Mockito.mock(Player.class);
        val uuid = UUID.randomUUID();
        Mockito.when(player.getName()).thenReturn("TestPlayer");
        Mockito.when(player.getUniqueId()).thenReturn(uuid);
        return player;
    }

    public static User mockUser()
    {
        User user = Mockito.mock(User.class);
        Player player = mockPlayer();
        DataMap dataMap = new DataMap();
        TimestampMap timestampMap = new TimestampMap();

        Mockito.when(user.getPlayer()).thenReturn(player);
        Mockito.when(user.getDataMap()).thenReturn(dataMap);
        Mockito.when(user.getTimestampMap()).thenReturn(timestampMap);
        return user;
    }

    public static ViolationModule mockViolationModule(String configString)
    {
        val vlModule = Mockito.mock(ViolationModule.class);
        Mockito.when(vlModule.getConfigString()).thenReturn(configString);
        return vlModule;
    }
}
