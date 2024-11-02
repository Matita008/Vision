package org.matita08.VisionMC.util;

import net.dv8tion.jda.api.entities.emoji.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.interactions.components.buttons.*;
import static org.matita08.VisionMC.Tools.*;

public class Staff {
  public static void verify(ButtonInteractionEvent e) {
  addRole(e.getGuild(),e.getUser(),1301300142573490219L);
  }
  
  public static void verify(SlashCommandInteractionEvent e) {
    e.getChannel().asTextChannel().sendMessage("Clicca il pulsante qua sotto per verificarti").addActionRow(Button.success("verify", "✅")).queue();
  }
}
