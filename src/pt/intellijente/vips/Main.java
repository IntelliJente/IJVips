package pt.intellijente.vips;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin {

    public static Main main;


    @Override
    public void onEnable() {
        getCommand("vip").setExecutor(new Comando());
        Bukkit.getPluginManager().registerEvents(new Evento(), this);
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("§4ERRO: §cHolographicDisplays não encontrado!");
            this.setEnabled(false);
            return;
        }
        Evento.privateHologram = new HashMap<>();
        saveDefaultConfig();
        main = this;
    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage("§aVerificacao de hologramas...");
        Map<Player, Hologram> localhash = Evento.privateHologram;

        if (localhash.isEmpty() == true) {
            Bukkit.getConsoleSender().sendMessage("§cNenhum holograma encontrado, desligando plugin...");
            return;
        }

        Bukkit.getConsoleSender().sendMessage("§cForam encontrados §7" + localhash.size() + "§c hologramas ativos");
        Bukkit.getConsoleSender().sendMessage("§cRemovendo os hologramas...");
        for (Map.Entry me : localhash.entrySet()) {

            if (localhash.containsKey(me.getKey())) {
                Hologram hologram = localhash.get(me.getKey());
                if (!(hologram.isDeleted())) {
                    hologram.delete();
                }
            }
        }
        Bukkit.getConsoleSender().sendMessage("§aTodos os hologramas foram deletados com sucesso! Desligando plugin...");
    }

    public static Main getPlugin() {
        return (Main) getPlugin(Main.class);
    }


}