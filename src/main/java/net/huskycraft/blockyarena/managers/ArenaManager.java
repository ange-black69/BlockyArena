/*
 * This file is part of BlockyArena, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 HuskyCraft <https://www.huskycraft.net>
 * Copyright (c) 2018 Darcy-Chen
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
package net.huskycraft.blockyarena.managers;

import net.huskycraft.blockyarena.arenas.Arena;
import net.huskycraft.blockyarena.arenas.ArenaState;
import net.huskycraft.blockyarena.BlockyArena;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * An ArenaManager keeps track of the available Arenas in the server.
 */
public class ArenaManager {

    public static BlockyArena plugin;

    private Map<String, Arena> arenas; // the list of Arenas available in the server

    public ArenaManager(BlockyArena plugin) {
        this.plugin = plugin;
        arenas = new HashMap<>();
        loadArenas();
    }

    /**
     * Reconstructs arenas from all arena config files in standard format.
     */
    private void loadArenas() {
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(plugin.getArenaDir(), "*.conf");
            for (Path path : stream) {
                Arena arena = new Arena(plugin, path);
                arenas.put(arena.getID(), arena);
            }
        } catch (IOException e) {
            plugin.getLogger().warn("Error loading existing arena configs.");
        }
    }

    /**
     * Gets an available arena from the list.
     * @return an enabled arena from the list, null if no arena is available
     */
    public Arena getArena() {
        for (Arena arena : arenas.values()) {
            if (arena.getState() == ArenaState.AVAILABLE) {
                return arena;
            }
        }
        return null;
    }

    /**
     * Gets the Arena with the given id.
     * @param id the identifier of the Arena
     * @return the Arena with the given id
     */
    public Arena getArena(String id) {
        return arenas.get(id);
    }

    /**
     * Adds the given Arena to the tracking list.
     * @param arena an Arena with any given state
     */
    public void add(Arena arena) {
        arenas.put(arena.getID(), arena);
    }
}