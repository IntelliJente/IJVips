package pt.intellijente.vips;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;


import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;


public class Comando implements CommandExecutor{


    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("vip")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Apenas players podem usar este comando!");
                return true;
            }
            Player player = (Player)sender;
            if (args.length == 0) {
                Inventory inv = Bukkit.createInventory(player, 9*Main.main.getConfig().getInt("GUI.Size"), Main.main.getConfig().getString("GUI.Name").replaceAll("&", "§"));
                for (String key : Main.main.getConfig().getConfigurationSection("Vips").getKeys(false)) {
                    int id = Main.main.getConfig().getInt("Vips."+key+".id");
                    int data = Main.main.getConfig().getInt("Vips."+key+".data");
                    String name = Main.main.getConfig().getString("Vips."+key+".name").replaceAll("&", "§");
                    ArrayList<String> l = new ArrayList();
                    ItemStack item = new ItemStack(id,1,(short) data);
                    ItemMeta meta = item.getItemMeta();
                    for(String lore:Main.main.getConfig().getStringList("Vips."+key+".lore")) {
                        l.add(lore.replace("&", "§"));
                    }
                    meta.setDisplayName(name);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    meta.setLore(l);
                    item.setAmount(1);
                    item.setItemMeta(meta);
                    if (Main.main.getConfig().getBoolean("Vips."+key+".glow")) {
                        item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
                    }
                    inv.setItem(Main.main.getConfig().getInt("Vips."+key+".slot"), item);
                }
                ItemStack t0 = new ItemStack(Main.main.getConfig().getInt("GUI.Fill.ID"),1,(short) Main.main.getConfig().getInt("GUI.Fill.Data"));
                ItemMeta t0meta = t0.getItemMeta();
                t0meta.setDisplayName(Main.main.getConfig().getString("GUI.Fill.name").replaceAll("&", "§"));
                t0.setItemMeta(t0meta);
                if (Main.main.getConfig().getBoolean("GUI.Fill.Activate")) {
                    for (int i = 0; i < inv.getSize(); i++) {
                        if (inv.getItem(i) == null) {
                            inv.setItem(i, t0);
                        }
                    }
                } else {}
                player.openInventory(inv);
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("IJVips.reload")) {
                    Main.main.reloadConfig();
                    player.sendMessage("§aConfig recarregada!");
                    return true;
                } else {
                    player.sendMessage("§cSem permissão!");
                    return true;
                }
            }

        }

        return false;
    }
}