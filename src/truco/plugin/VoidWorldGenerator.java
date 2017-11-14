/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truco.plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

/**
 *
 * @author Gabriel
 */
  public class VoidWorldGenerator    extends ChunkGenerator
  {
    public VoidWorldGenerator() {}
    
    @Override
     public List<BlockPopulator> getDefaultPopulators(World world)
    {
      return Arrays.asList(new BlockPopulator[0]);
    }
    
    @Override
    public boolean canSpawn(World world, int x, int z)
    {
      return true;
    }
    
    @Override
    public byte[] generate(World world, Random rand, int chunkx, int chunkz)
    {
      return new byte[32768];
    }
    
    @Override
    public Location getFixedSpawnLocation(World world, Random random)
    {
      return new Location(world, 0.0D, 128.0D, 0.0D);
    }
  }
