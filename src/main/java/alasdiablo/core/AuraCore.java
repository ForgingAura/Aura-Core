package alasdiablo.core;

import alasdiablo.core.config.AuraConfig;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod.CustomProperty;
import net.minecraftforge.fml.common.Mod.Instance;
import alasdiablo.core.proxy.Common;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlasDiablo on 31/08/2017.
 */
@Mod(modid = AuraCore.MODID, name = AuraCore.NAME, version = AuraCore.VERSION, acceptedMinecraftVersions = AuraCore.ACCEPTED_VERSION, useMetadata = true,
        customProperties = { @CustomProperty(k = "useVersionChecker", v = "true") })
public class AuraCore {

    public static final List<String> DEPENDANTS = new ArrayList<String>();

    @Instance
    public static AuraCore instance;

    public static final String MODID = "auracore";
    public static final String NAME = "Aura Core";
    public static final String VERSION = "1.0";
    public static final String ACCEPTED_VERSION = "[1.11,1.11.2]";
    public static final String CLIENT_PROXY_CLASSE = "alasdiablo.core.proxy.Client";
    public static final String SERVER_PROXY_CLASSE = "alasdiablo.core.proxy.Server";
    public static final Logger logger = LogManager.getFormatterLogger(NAME);

    private static void updateDependants() {
        AuraConfig.UPDATE_CHECKER_MODS.put(MODID, true);
        for (ModContainer mod : Loader.instance().getActiveModList()) {
            for (ArtifactVersion version : mod.getDependencies()) {
                if (version.getLabel().equals(MODID)) {
                    if (!DEPENDANTS.contains(mod.getModId())) {
                        DEPENDANTS.add(mod.getModId());
                        if (mod.getCustomModProperties().containsKey("useVersionChecker")) {
                            if (Boolean.valueOf(mod.getCustomModProperties().get("useVersionChecker"))) {
                                if (!AuraConfig.UPDATE_CHECKER_MODS.containsKey(mod.getModId())) {
                                    AuraConfig.UPDATE_CHECKER_MODS.put(mod.getModId(), true);
                                }
                            }
                        } else {
                            logger.error("Mod " + mod.getModId() + " does not say whether it uses an version checker! Please fix this!");
                            FMLCommonHandler.instance().exitJava(0, false);
                        }
                    }
                }
            }
            for (ArtifactVersion version : mod.getRequirements()) {
                if (version.getLabel().equals(MODID)) {
                    if (!DEPENDANTS.contains(mod.getModId())) {
                        DEPENDANTS.add(mod.getModId());
                        if (mod.getCustomModProperties().containsKey("useVersionChecker")) {
                            if (Boolean.valueOf(mod.getCustomModProperties().get("useVersionChecker"))) {
                                if (!AuraConfig.UPDATE_CHECKER_MODS.containsKey(mod.getModId())) {
                                    AuraConfig.UPDATE_CHECKER_MODS.put(mod.getModId(), true);
                                }
                            }
                        } else {
                            logger.error("Mod " + mod.getModId() + " does not say whether it uses an version checker! Please fix this!");
                            FMLCommonHandler.instance().exitJava(0, false);
                        }
                    }
                }
            }
        }
        DEPENDANTS.forEach(mod -> {
            AuraCore.logger.info("Found dependant: " + mod);
        });
        AuraConfig.UPDATE_CHECKER_MODS.forEach((key, value) -> {
            AuraCore.logger
                    .info("Mod " + key + " says it has a version checker!");
        });
    }

    @SidedProxy(clientSide = AuraCore.CLIENT_PROXY_CLASSE, serverSide = AuraCore.SERVER_PROXY_CLASSE)
    public static Common proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        updateDependants();
        AuraConfig.preInit();
        proxy.preInit();
        System.out.println("Pre Initialization of AuraCore Done");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        System.out.println("Initialization of AuraCore Done");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("Post Initialization of AuraCore Done");
    }

}