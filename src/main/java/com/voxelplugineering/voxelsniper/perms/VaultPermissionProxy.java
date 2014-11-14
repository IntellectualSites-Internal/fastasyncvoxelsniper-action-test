/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 The Voxel Plugineering Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.voxelplugineering.voxelsniper.perms;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.voxelplugineering.voxelsniper.api.IPermissionProxy;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.bukkit.BukkitSniper;
import com.voxelplugineering.voxelsniper.common.CommonWorld;

public class VaultPermissionProxy implements IPermissionProxy
{

    private static Permission permissionService;

    public VaultPermissionProxy()
    {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
        permissionService = rsp.getProvider();
    }

    @Override
    public boolean isOp(ISniper sniper)
    {
        return sniper instanceof BukkitSniper && ((BukkitSniper) sniper).getPlayerReference().isOp();
    }

    @Override
    public boolean hasPermission(ISniper sniper, String permission)
    {
        return sniper instanceof BukkitSniper && permissionService.playerHas(((BukkitSniper) sniper).getPlayerReference(), permission);
    }

    @Override
    public boolean hasWorldPermission(ISniper sniper, CommonWorld world, String permission)
    {
        return sniper instanceof BukkitSniper
                && permissionService.playerHas(world.getName(), ((BukkitSniper) sniper).getPlayerReference(), permission);
    }

    @Override
    public boolean hasWorldPermission(ISniper sniper, String worldName, String permission)
    {
        return sniper instanceof BukkitSniper && permissionService.playerHas(worldName, ((BukkitSniper) sniper).getPlayerReference(), permission);
    }

    @Override
    public void addGlobalPermission(ISniper sniper, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerAdd(((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void addWorldPermission(ISniper sniper, CommonWorld world, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerAdd(world.getName(), ((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void addWorldPermission(ISniper sniper, String worldName, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerAdd(worldName, ((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void addTransientGlobalPermission(ISniper sniper, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerAddTransient(((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void addTransientWorldPermission(ISniper sniper, CommonWorld world, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerAddTransient(world.getName(), ((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void addTransientWorldPermission(ISniper sniper, String worldName, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerAddTransient(worldName, ((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void removeGlobalPermission(ISniper sniper, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerRemove(((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void removeWorldPermission(ISniper sniper, CommonWorld world, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerRemove(world.getName(), ((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void removeWorldPermission(ISniper sniper, String worldName, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerRemove(worldName, ((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void removeTransientGlobalPermission(ISniper sniper, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerRemoveTransient(((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void removeTransientWorldPermission(ISniper sniper, CommonWorld world, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerRemoveTransient(world.getName(), ((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }

    @Override
    public void removeTransientWorldPermission(ISniper sniper, String worldName, String permission)
    {
        if (sniper instanceof BukkitSniper)
        {
            permissionService.playerRemoveTransient(worldName, ((BukkitSniper) sniper).getPlayerReference(), permission);
        }
    }
}