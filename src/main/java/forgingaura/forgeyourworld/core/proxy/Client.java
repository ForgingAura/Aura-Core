package forgingaura.forgeyourworld.core.proxy;

import forgingaura.forgeyourworld.core.config.ForgeYourWorldConfig;
import forgingaura.forgeyourworld.core.event.ForgeYourWorldEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by AlasDiablo on 31/08/2017.
 */
public class Client implements Common{

    @Override
    public void preInit() {
        ForgeYourWorldConfig.clientPreInit();
        MinecraftForge.EVENT_BUS.register(new ForgeYourWorldEvent());

    }

    @Override
    public void init() {

    }

}
