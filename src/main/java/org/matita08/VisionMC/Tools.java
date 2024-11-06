package org.matita08.VisionMC;

import net.dv8tion.jda.api.entities.*;
import static org.matita08.VisionMC.Settings.*;

public class Tools {
  public static void updateActivity(Ibot b) {
    b.getShardManager().setActivity(Activity.watching(server == null ? "non sono in Vision" : server.getMemberCount() + " Vision members"));
  }
  
  public static void addRole(Guild g, User u, long role) {
    addRole(g, u, g.getRoleById(role));//TODO also a remove would be usefull
                                        //TODO maybe also a isPresent(Role,Member,Guild)
  }
  
  public static void addRole(Guild g, User u, Role r) {
    g.addRoleToMember(u, r).queue();
  }
}
