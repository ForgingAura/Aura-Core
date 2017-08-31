package alasdiablo.core.proxy;

import alasdiablo.core.config.AuraConfig;
import alasdiablo.core.event.AuraEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by AlasDiablo on 31/08/2017.
 */
public class Client implements Common{

    @Override
    public void preInit() {
        AuraConfig.clientPreInit();
        MinecraftForge.EVENT_BUS.register(new AuraEvent());

    }

    @Override
    public void init() {

    }

}
