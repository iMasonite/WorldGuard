/*
 * WorldGuard Copyright (C) 2012 sk89q <http://www.sk89q.com> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldguard.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sk89q.commandbook.InfoComponent;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/** @author zml2008 */
public class WorldGuardCommandBookListener implements Listener {
	private final WorldGuardPlugin plugin;
	
	public WorldGuardCommandBookListener(WorldGuardPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerWhois(InfoComponent.PlayerWhoisEvent event) {
		if (event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			LocalPlayer localPlayer = plugin.wrapPlayer(player);
			if (plugin.getGlobalStateManager().get(player.getWorld()).useRegions) {
				ApplicableRegionSet regions = plugin.getGlobalRegionManager().get(player.getWorld()).getApplicableRegions(player.getLocation());
				
				// Current regions
				StringBuilder regionStr = new StringBuilder();
				boolean first = true;
				
				for (ProtectedRegion region : regions) {
					if (!first) {
						regionStr.append(", ");
					}
					
					if (region.isOwner(localPlayer)) {
						regionStr.append("+");
					}
					else if (region.isMemberOnly(localPlayer)) {
						regionStr.append("-");
					}
					
					regionStr.append(region.getId());
					
					first = false;
				}
				
				if (regions.size() > 0) {
					event.addWhoisInformation("Current Regions", regionStr);
				}
				event.addWhoisInformation("Can build", regions.canBuild(localPlayer));
			}
		}
	}
}
