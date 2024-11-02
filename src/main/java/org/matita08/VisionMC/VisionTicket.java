package org.matita08.VisionMC;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.*;
import net.dv8tion.jda.api.sharding.*;
import static org.matita08.VisionMC.Private.*;
import static org.matita08.VisionMC.Settings.*;
import org.matita08.VisionMC.listener.*;

public class VisionTicket extends Ibot {
  public VisionTicket() {
    ticket = this;
    DefaultShardManagerBuilder b = DefaultShardManagerBuilder.createDefault(ticketToken);
    b.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES);
    b.setActivity(Activity.watching("Tickets"));
    b.addEventListeners(new TicketListener());
    shard = b.build();
  }
}