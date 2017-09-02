package forgingaura.forgeyourworld.core;

import forgingaura.forgeyourworld.core.config.ForgeYourWorldConfig;
import forgingaura.forgeyourworld.core.proxy.Common;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod.CustomProperty;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlasDiablo on 31/08/2017.
 */
@Mod(modid = ForgeYourWorldCore.MODID, name = ForgeYourWorldCore.NAME, version = ForgeYourWorldCore.VERSION, acceptedMinecraftVersions = ForgeYourWorldCore.ACCEPTED_VERSION, useMetadata = true,
        customProperties = { @CustomProperty(k = "useVersionChecker", v = "true") })
public class ForgeYourWorldCore {

    public static final List<String> DEPENDANTS = new ArrayList<String>();

    @Instance
    public static ForgeYourWorldCore instance;

    public static final String MODID = "forgeyourworldcore";
    public static final String NAME = "Forge Your World Core";
    public static final String VERSION = "beta-2.0";
    public static final String ACCEPTED_VERSION = "[1.11,1.11.2]";
    public static final String CLIENT_PROXY_CLASSE = "forgingaura.forgeyourworld.core.proxy.Client";
    public static final String SERVER_PROXY_CLASSE = "forgingaura.forgeyourworld.core.proxy.Server";
    public static final Logger logger = LogManager.getFormatterLogger(NAME);

    private static void updateDependants() {
        ForgeYourWorldConfig.UPDATE_CHECKER_MODS.put(MODID, true);
        for (ModContainer mod : Loader.instance().getActiveModList()) {
            for (ArtifactVersion version : mod.getDependencies()) {
                if (version.getLabel().equals(MODID)) {
                    if (!DEPENDANTS.contains(mod.getModId())) {
                        DEPENDANTS.add(mod.getModId());
                        if (mod.getCustomModProperties().containsKey("useVersionChecker")) {
                            if (Boolean.valueOf(mod.getCustomModProperties().get("useVersionChecker"))) {
                                if (!ForgeYourWorldConfig.UPDATE_CHECKER_MODS.containsKey(mod.getModId())) {
                                    ForgeYourWorldConfig.UPDATE_CHECKER_MODS.put(mod.getModId(), true);
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
                                if (!ForgeYourWorldConfig.UPDATE_CHECKER_MODS.containsKey(mod.getModId())) {
                                    ForgeYourWorldConfig.UPDATE_CHECKER_MODS.put(mod.getModId(), true);
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
            ForgeYourWorldCore.logger.info("Found dependant: " + mod);
        });
        ForgeYourWorldConfig.UPDATE_CHECKER_MODS.forEach((key, value) -> {
            ForgeYourWorldCore.logger
                    .info("Mod " + key + " says it has a version checker!");
        });
    }

    @SidedProxy(clientSide = ForgeYourWorldCore.CLIENT_PROXY_CLASSE, serverSide = ForgeYourWorldCore.SERVER_PROXY_CLASSE)
    public static Common proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        updateDependants();
        ForgeYourWorldConfig.preInit();
        proxy.preInit();
        System.out.println("Pre Initialization of Forge Your World Core Done");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        System.out.println("Initialization of Forge Your World Core Done");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("Post Initialization of Forge Your World Core Done");
    }

}