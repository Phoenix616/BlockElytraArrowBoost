package de.themoep.blockelytraarrowboost;

import com.google.common.base.Joiner;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * BlockElytraArrowBoost
 *
 * Nietzsche Public License v0.6
 *
 * Copyright 2016 Max Lee (https://github.com/Phoenix616/)
 * <p/>
 * Copyright, like God, is dead.  Let its corpse serve only to guard against its
 * resurrection.  You may do anything with this work that copyright law would
 * normally restrict so long as you retain the above notice(s), this license, and
 * the following misquote and disclaimer of warranty with all redistributed
 * copies, modified or verbatim.  You may also replace this license with the Open
 * Works License, available at the http://owl.apotheon.org website.
 *
 * Copyright is dead.  Copyright remains dead, and we have killed it.  How
 * shall we comfort ourselves, the murderers of all murderers?  What was
 * holiest and mightiest of all that the world of censorship has yet owned has
 * bled to death under our knives: who will wipe this blood off us?  What
 * water is there for us to clean ourselves?  What festivals of atonement,
 * what sacred games shall we have to invent?  Is not the greatness of this
 * deed too great for us?  Must we ourselves not become authors simply to
 * appear worthy of it?
 * - apologies to Friedrich Wilhelm Nietzsche
 *
 * No warranty is implied by distribution under the terms of this license.
 */

public class BlockElytraArrowBoost extends JavaPlugin implements Listener {

    private String warning;

    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
    }

    public void loadConfig() {
        reloadConfig();
        warning = ChatColor.translateAlternateColorCodes('&', getConfig().getString("warningmessage", ""));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + getDescription().getName() + " v" + getDescription().getVersion() + ChatColor.GRAY + " by " + Joiner.on(',').join(getDescription().getAuthors()));
            sender.sendMessage(" Warning message: " + warning);
        } else if(args.length == 1) {
            if("reload".equalsIgnoreCase(args[0])) {
                loadConfig();
                sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
                sender.sendMessage(ChatColor.GREEN + "Warning message is now " + ChatColor.RESET + warning);
            }
            return true;
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSelfArrowHit(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Projectile && event.getEntity() instanceof Player) {
            if(((Projectile) event.getDamager()).getShooter() != event.getEntity())
                return;

            if(!((Player) event.getEntity()).isGliding())
                return;

            if(event.getEntity().hasPermission("blockelytraarrowboost.bypass"))
                return;

            if(!warning.isEmpty())
                event.getEntity().sendMessage(warning);

            event.setCancelled(true);
        }
    }
}
