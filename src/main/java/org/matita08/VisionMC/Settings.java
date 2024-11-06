package org.matita08.VisionMC;

import net.dv8tion.jda.api.entities.*;

public class Settings {
  public static VisionBots bots;
  public static VisionTicket ticket;
  public static VisionStaff staff;
  
  public static Guild server = null;
  public static Role verified = null;
  public static Role notVerified = null;
  
  public static long serverId = 0;
  public static long verifiedId = 0;
  public static long notVerifiedId = 0;
  
  public static void init(Ibot b) {
    if(server == null) server = b.getShardManager().getGuildById(serverId);
    if(verified == null) verified = b.getShardManager().getRoleById(verifiedId);
    if(notVerified == null) notVerified = b.getShardManager().getRoleById(notVerifiedId);
  }
  
  public static void initAll(Ibot b) {
    server = b.getShardManager().getGuildById(serverId);
    verified = b.getShardManager().getRoleById(verifiedId);
    notVerified = b.getShardManager().getRoleById(notVerifiedId);
  }
}
