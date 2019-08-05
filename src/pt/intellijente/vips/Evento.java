package pt.intellijente.vips;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

public class Evento implements Listener {

    public static Map<Player, Hologram> privateHologram;

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Location userLocation = player.getLocation().add(0, Main.main.getConfig().getInt("Height"), 0).clone();
        userLocation.setPitch(0F);
        Vector direction = userLocation.clone().getDirection();

        Vector position = direction.clone().multiply(2.6).setY(0).add(direction.clone().crossProduct(new Vector(0, 1, 0)).multiply(0));
        Location location = userLocation.clone().add(0, .4, 0).add(position);
        Hologram hologram = HologramsAPI.createHologram(Main.main, location);
        if (e.getInventory().getTitle().equalsIgnoreCase(Main.main.getConfig().getString("GUI.Name").replaceAll("&", "§"))) {
            e.setCancelled(true);
            for (String key : Main.main.getConfig().getConfigurationSection("Vips").getKeys(false)) {
                if (e.getSlot() == Main.main.getConfig().getInt("Vips."+key+".slot")) {
                    List<String> h = Main.main.getConfig().getStringList("Vips." + key + ".hologram");
                    for (int i = 0; i < h.size(); i++) {
                        hologram.insertTextLine(i, h.get(i).replace('&', '§'));
                    }
                    if (privateHologram.containsKey(player)) {
                        List<String> messages = Main.main.getConfig().getStringList("Message.Messages");
                        for (String s : messages) {
                            if (Main.main.getConfig().getBoolean("Message.Activate")) {
                                player.sendMessage(s.replaceAll("&","§").replaceAll("%price%", String.valueOf(Main.main.getConfig().getInt("Vips."+key+".price"))));
                            } else {}
                        }
                        privateHologram.get(player).delete();
                        privateHologram.replace(player, hologram);
                        player.closeInventory();
                        return;
                    }
                    privateHologram.put(player, hologram);
                    List<String> messages = Main.main.getConfig().getStringList("Message.Messages");
                    for (String s : messages) {
                        if (Main.main.getConfig().getBoolean("Message.Activate")) {
                            player.sendMessage(s.replaceAll("&","§").replaceAll("%price%", String.valueOf(Main.main.getConfig().getInt("Vips."+key+".price"))));
                        } else {}
                    }
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        int x = Main.main.getConfig().getInt("Distance");
        Player player = event.getPlayer();
        if (privateHologram.containsKey(player)) {
            Hologram hologram = privateHologram.get(player);
            if (hologram.getLocation().distance(player.getLocation()) >= x) {
                hologram.delete();
                privateHologram.remove(player);
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Hologram hologram = privateHologram.get(player);
        if (privateHologram.containsKey(player)) {
            hologram.delete();
            privateHologram.remove(player);
        } else {}
    }

    @EventHandler
    public void onDead(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Hologram hologram = privateHologram.get(player);
        if (privateHologram.containsKey(player)) {
            hologram.delete();
            privateHologram.remove(player);
        } else {}
    }


}