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
import static org.matita08.VisionMC.util.Ticket.*;

public class TicketListener extends ListenerAdapter {
  Ibot bot = Settings.ticket;
  
  public TicketListener() {}
  
  @Override
  public void onReady(@NotNull ReadyEvent event) {
    for (Guild g: bot.getShardManager().getGuilds()) {
      setSlash(g);
    }
    init(bot);
    updateActivity(bot);
    System.out.println(bot.getShardManager().getGuilds());
    System.out.println("loaded");
  }
  
  private static void setSlash(Guild g) {
    g.updateCommands().addCommands(Commands.slash("ticket",
                    "Send the ticket embed here").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(new OptionData(OptionType.CHANNEL,
                    "channel",
                    "lascia vuoto, DEBUG ONLY!").setChannelTypes(ChannelType.TEXT)),
            Commands.slash("close", "Close the ticket"),
            Commands.slash("t", "DEBUG ONLY!"),
            Commands.slash("add", "Add a people to the current ticket").addOption(OptionType.USER, "to", "la persona da aggiungere"),
            Commands.slash("transfer", "Transfer the current ticket").addOption(OptionType.USER, "to", "la persona a cui trasferire il ticket"),
            Commands.slash("claim", "Claim the current ticket"),
            Commands.slash("unclaim", "Unclaim the current ticket")).queue();
  }
  
  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    switch(event.getName().toLowerCase()) {
      case "ticket" -> sendTicketEmbed(event);
      case "close" -> closeTicket(event);
      case "claim" -> claimTicket(event);
      case "unclaim" -> unclaimTicket(event);
      case "transfer" -> transferTicket(event);
      case "add" -> addToTicket(event);
      //case "t" -> updCatRole(event);
      //case "t"-> sendNewTicket(event.getChannel().asTextChannel(), event.getMember(), event.getMember().getRoles().get(0));
      // case "t" -> ;
    }
    //System.out.println(event+event.getName());
  }
  
  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    switch(event.getButton().getId().toLowerCase()) {
      case "claim" -> claimTicket(event);
      case "close" -> closeTicket(event);
    }
    //System.out.println(event.getButton().getId());
  }
  
  @Override
  public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
    //if(event.getInteraction())//TODO
    openTicket(event);
  }
  
}
