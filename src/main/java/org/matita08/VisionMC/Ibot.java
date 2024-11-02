package org.matita08.VisionMC;

import net.dv8tion.jda.api.sharding.*;

public abstract class Ibot {
  ShardManager shard;
  
  public ShardManager getShardManager() {
    return shard;
  }
}
