package org.matita08.VisionMC;

import net.dv8tion.jda.api.entities.*;

public class Tools {
  public static void updateActivity(Ibot b) {
    b.getShardManager().setActivity(Activity.watching(b.getShardManager().getGuildById(1299516238686458058L) == null ? "non sono in Vision" : b.getShardManager().getGuildById(
            1299516238686458058L).getMemberCount() + " Vision members"));
  }
  public static void addRole(Guild g, User u,Role r){
    g.addRoleToMember(u,r).queue();
  }
  public static void addRole(Guild g, User u,long role){
    addRole(g,u,g.getRoleById(role));
  }
  
}
