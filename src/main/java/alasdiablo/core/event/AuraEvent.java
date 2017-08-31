package alasdiablo.core.event;

import alasdiablo.core.AuraCore;
import alasdiablo.core.util.UpdateChecker;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by AlasDiablo on 31/08/2017.
 */
public class AuraEvent {

    @SubscribeEvent
    public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        UpdateChecker.checkForUpdate(UpdateChecker.auracoreURL, AuraCore.MODID, AuraCore.VERSION, event.player);
    }
}
