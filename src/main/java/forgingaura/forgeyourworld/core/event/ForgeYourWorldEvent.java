package forgingaura.forgeyourworld.core.event;

import forgingaura.forgeyourworld.core.ForgeYourWorldCore;
import forgingaura.forgeyourworld.core.util.VersionChecker;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by AlasDiablo on 31/08/2017.
 */
public class ForgeYourWorldEvent {

    @SubscribeEvent
    public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        VersionChecker.checkForUpdate(VersionChecker.forgeyourworldcoreURL, ForgeYourWorldCore.MODID, ForgeYourWorldCore.VERSION, event.player);
    }
}
