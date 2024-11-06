package org.matita08.VisionMC.listener;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.events.session.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.*;
import org.jetbrains.annotations.*;
import org.matita08.VisionMC.*;
import static org.matita08.VisionMC.Settings.*;
import static org.matita08.VisionMC.Tools.*;
import static org.matita08.VisionMC.util.Staff.*;

public class StaffListener extends ListenerAdapter {
  Ibot bot = Settings.staff;
  
  public StaffListener() {}
  
  @Override
  public void onReady(@NotNull ReadyEvent event) {
    init(bot);
    for (Guild g: bot.getShardManager().getGuilds()) {
      setSlash(g);
    }
    updateActivity(bot);
    System.out.println(bot.getShardManager().getGuilds());
    System.out.println("loaded");
  }
  
  private static void setSlash(Guild g) {
    g.updateCommands().addCommands(Commands.slash("verify",
            "Send the verify message here").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(new OptionData(OptionType.CHANNEL,
            "channel",
            "lascia vuoto, DEBUG ONLY!").setChannelTypes(ChannelType.TEXT))).queue();
  }
  
  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    switch(event.getName().toLowerCase()) {
      case "verify" -> verify(event);
    }
  }
  
  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    String id = event.getButton().getId();
    if(id == null) return;
    if(id.equalsIgnoreCase("verify")) verify(event);
    else if(id.contains("role")) {
    
    }
    //System.out.println(event.getButton().getId());
  }
}
