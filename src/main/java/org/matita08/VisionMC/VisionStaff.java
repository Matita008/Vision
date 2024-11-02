package org.matita08.VisionMC;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.*;
import net.dv8tion.jda.api.sharding.*;
import static org.matita08.VisionMC.Private.*;
import static org.matita08.VisionMC.Settings.*;
import org.matita08.VisionMC.listener.*;

public class VisionStaff extends Ibot{
  public VisionStaff() {
    staff = this;
    DefaultShardManagerBuilder b = DefaultShardManagerBuilder.createDefault(staffToken);
    b.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES,GatewayIntent.GUILD_EMOJIS_AND_STICKERS);
    b.setActivity(Activity.watching("Staff"));
    b.addEventListeners(new StaffListener ());
    shard = b.build();
  }
}
