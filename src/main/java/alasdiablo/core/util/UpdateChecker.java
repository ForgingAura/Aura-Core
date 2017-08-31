package alasdiablo.core.util;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Action;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import alasdiablo.core.AuraCore;
import alasdiablo.core.config.AuraConfig;
import joptsimple.internal.Strings;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

/**
 * Handles any updates required using a custom JSON file hosted on a webserver
 *
 * @author CJMinecraft
 *
 */
public class UpdateChecker {

    /**
     * The URL for the CJCore update json file
     */
    public static final String auracoreURL = "https://raw.githubusercontent.com/ForgingAura/Aura-Core/master/update.json";

    /**
     * States whether an update is available using the update json file
     *
     * @param url
     *            The url where the update file is found
     * @param currentVersion
     *            The current version of the mod
     * @return Whether an update is available
     */
    public static boolean isUpdate(String url, String currentVersion) {
        try {
            URL URL = new URL(url);
            JsonReader jr = new JsonReader(new InputStreamReader(URL.openStream()));
            JsonElement je = new JsonParser().parse(jr);
            JsonObject jo = je.getAsJsonObject();
            String version = jo.get("version").getAsString();
            if (!version.equals(currentVersion))
                return true;
        } catch (Exception e) {
            AuraCore.logger.catching(e);
        }
        return false;
    }

    /**
     * Will display a message when there is an update available using
     * information from the update file
     *
     * @param url
     *            The url path to the file
     * @param modid
     *            So that the update checker can be disabled in the config
     * @param currentVersion
     *            The current mod version
     * @param player
     *            The player to send the messages to
     */
    public static void checkForUpdate(String url, String modid, String currentVersion, EntityPlayer player) {
        if (!AuraConfig.UPDATE_CHECKER_MODS.containsKey(modid) || !AuraConfig.UPDATE_CHECKER_MODS.get(modid))
            return;
        String version = "";
        String name = "";
        List<String> changeLog = new ArrayList<String>();
        String downloadURL = "";
        boolean updateRequired = false;
        try {
            URL URL = new URL(url);
            JsonReader jr = new JsonReader(new InputStreamReader(URL.openStream()));
            JsonElement je = new JsonParser().parse(jr);
            JsonObject jo = je.getAsJsonObject();
            version = jo.get("version").getAsString();
            if (!version.equals(currentVersion))
                updateRequired = true;
            if (updateRequired) {
                name = jo.get("name").getAsString();
                JsonArray cl = jo.get("changelog").getAsJsonArray();
                for (JsonElement e : cl) {
                    changeLog.add(e.getAsString());
                }
                if(jo.has("download"))
                    downloadURL = jo.get("download").getAsString();
            }
        } catch (Exception e) {
            AuraCore.logger.info("Error reading update url: " + url);
            AuraCore.logger.catching(e);
            return;
        }
        if (updateRequired) {
            player.sendMessage(new TextComponentString(TextFormatting.WHITE
                    + I18n.format("update.ready", TextFormatting.GOLD + name + TextFormatting.WHITE)));
            player.sendMessage(new TextComponentString(TextFormatting.WHITE
                    + I18n.format("update.version", TextFormatting.DARK_RED + currentVersion + TextFormatting.WHITE,
                    TextFormatting.DARK_GREEN + version)));
            ITextComponent changeLogAndVersion = new TextComponentString("");
            if (!Strings.isNullOrEmpty(downloadURL))
                changeLogAndVersion.appendSibling(new TextComponentString(TextFormatting.WHITE + "["
                        + TextFormatting.DARK_AQUA + I18n.format("update.download") + TextFormatting.WHITE + "] ")
                        .setStyle(new Style()
                                .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadURL))));
            if (changeLog.size() > 0) {
                String changeLogString = "";
                for (String log : changeLog) {
                    if (log.startsWith("="))
                        changeLogString += TextFormatting.AQUA;
                    else if (log.startsWith("-"))
                        changeLogString += TextFormatting.RED;
                    else if (log.startsWith("+"))
                        changeLogString += TextFormatting.GREEN;
                    else
                        changeLogString += TextFormatting.WHITE;
                    changeLogString += log + "\n";
                }
                changeLogString = changeLogString.substring(0, changeLogString.length() - 1);
                changeLogAndVersion.appendSibling(new TextComponentString(
                        "[" + TextFormatting.DARK_AQUA + I18n.format("update.changelog") + TextFormatting.WHITE + "]")
                        .setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TextComponentString(changeLogString)))));
            }
            player.sendMessage(changeLogAndVersion);
        }
    }

}
