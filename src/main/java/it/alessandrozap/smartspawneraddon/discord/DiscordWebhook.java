package it.alessandrozap.smartspawneraddon.discord;

import it.alessandrozap.smartspawneraddon.SmartSpawnerAddon;
import it.alessandrozap.smartspawneraddon.config.Settings;
import it.alessandrozap.smartspawneraddon.objects.ActionType;
import static it.alessandrozap.smartspawneraddon.config.Settings.Hooks.Discord;

import it.alessandrozap.utilsapi.logger.LogType;
import it.alessandrozap.utilsapi.logger.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DiscordWebhook {

    public static void sendEmbed(Player player, Location location, ActionType type) {
        if(!Settings.Hooks.Discord.Webhooks.isEnabled()) return;
        String webhookURL = Discord.Webhooks.getUrl();
        if(webhookURL == null || webhookURL.isEmpty()) return;
        if(!Discord.Webhooks.getActions().containsKey(type.name().toLowerCase())) return;
        new BukkitRunnable() {
            public void run() {
                HttpsURLConnection connection = null;
                try {
                    List<String> description = new ArrayList<>();

                    for (String str : Discord.Webhooks.getLines()) {
                        description.add(str.replaceAll("%player%", player != null ? player.getName() : "Unknown")
                                .replaceAll("%action%", type.name())
                                .replaceAll("%world%", location.getWorld().getName())
                                .replaceAll("%x%", String.valueOf(location.getBlockX()))
                                .replaceAll("%y%", String.valueOf(location.getBlockY()))
                                .replaceAll("%z%", String.valueOf(location.getBlockZ()))
                        );
                    }

                    URL url = new URL(Discord.Webhooks.getUrl());
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setDoOutput(true);

                    String title = Discord.Webhooks.getTitle().replaceAll("%action%", type.name());

                    String jsonPayload = "{"
                            + "\"embeds\": ["
                            + "  {"
                            + "    \"title\": \"" + escapeJson(title) + "\","
                            + "    \"description\": \"" + escapeJson(String.join("\n", description)) + "\","
                            + "    \"color\": " + Integer.parseInt(Discord.Webhooks.getActions().get(type.name().toLowerCase()).getColor(), 16)
                            + "  }"
                            + "]"
                            + "}";

                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode != 200 && responseCode != 204) {
                        InputStream errorStream = connection.getErrorStream();
                        if (errorStream != null) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line).append("\n");
                            }
                            reader.close();
                            Logger.log("[DiscordWebhook] HTTP error " + responseCode + ":\n" + response, LogType.ERROR);
                        } else Logger.log("[DiscordWebhook] HTTP error " + responseCode + ", nessuna risposta nel body.", LogType.ERROR);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (connection != null) connection.disconnect();
                }
            }
        }.runTaskAsynchronously(SmartSpawnerAddon.getInstance());
    }

    private static String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "\\t")
                .replace("\r", "\\r");
    }
}