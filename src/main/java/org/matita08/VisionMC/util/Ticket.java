package org.matita08.VisionMC.util;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.entities.channel.middleman.*;
import net.dv8tion.jda.api.entities.emoji.*;
import net.dv8tion.jda.api.events.interaction.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.*;
import net.dv8tion.jda.api.managers.channel.concrete.*;
import org.jetbrains.annotations.*;
import static org.matita08.VisionMC.Settings.*;

import java.awt.*;
import java.util.*;
import java.util.concurrent.*;


public class Ticket {
  public static EnumSet<Permission> read = EnumSet.of(Permission.VIEW_CHANNEL,
          Permission.MESSAGE_SEND,
          Permission.MESSAGE_HISTORY,
          Permission.MESSAGE_MANAGE);
  
  public static void sendTicketEmbed(@NotNull SlashCommandInteractionEvent event) {
    //Channel ch = event.getOption("channel").getAsChannel();if(ch == null)
    TextChannel c = event.getGuild().getTextChannelById(1299547580224634971L);
    if(!c.canTalk()) {
      event.reply("i can't send messages/embed").setEphemeral(true).queue();
      return;
    }
    event.reply("sending ticket embed in " + c.getAsMention()).setEphemeral(true).queue();
    EmbedBuilder e = new EmbedBuilder();
    e.setColor(Color.ORANGE);
    e.setTitle("<:phone:1301318052817932318> VisionMC┃Ticket<:star:1301316991440912384> ");
    e.setDescription(
            "Ti serve aiuto❓»\nNessun problema!  Seleziona la categoria qua sotto per creare un nuovo ticket dove il nostro staff ti assisterà.\n<:attention:1301318210204733490> Se aprirai un ticket per TROLL ci sarà una conseguenza come un ban o un mute in base all utilizzo <:exlamation_mark:1301318181041864744>");
    e.setFooter(" ");
    c.sendMessageEmbeds(e.build()).addActionRow(StringSelectMenu.create("Ticket").setPlaceholder("Seleziona la categoria").addOption(
            "Appella un Ban/Mute",
            "appeal",
            "→Richiedi un unban/unmute",
            Emoji.fromCustom("pixel_exclamation", 1300507386099798036L, false)).addOption("Candidatura Staffer/Media",
            "Candidatura",
            "→Candidati per una posizione disponibile",
            Emoji.fromCustom("staff", 1301318147940421754L, false)).addOption("Partnership",
            "Partnership",
            "→Richiedi qua una partnership",
            Emoji.fromFormatted("<:star:1301316991440912384>")).addOption("Segnala Player/Bug",
            "Segnalazione",
            Emoji.fromCustom("attention", 1301318210204733490L, false)).addOption("Acquisto Vip/Bundle",
            "Shop",
            "→Compra qualcosa dallo store",
            Emoji.fromFormatted("<:money:1301318103069753405>")).addOption("Altro",
            "Altro",
            "Qualunque altra cosa non inclusa",
            Emoji.fromFormatted("<:noteblock:1301318257466409013>")).build()).queue();
  }
  
  public static void openTicket(@NotNull StringSelectInteractionEvent e) {
    if(e.getGuild() == null || e.getMember() == null) {
      e.reply("an error occurred").queue();
      System.out.println(e);
      return;
    }
    //e.reply("elaborating...").setEphemeral(true).queue();
    if(!e.getComponentId().equals("Ticket")) System.out.println(e);
    e.getSelectedOptions().forEach(option->e.reply(createTicket(option, e.getGuild(), e.getMember())).setEphemeral(true).queue());
  }
  
  @SuppressWarnings("Unused")
  public static String createTicket(@NotNull SelectOption o, Guild g, @NotNull Member m) {
    Category c;
    String name = "Ticket-" + o.getValue();
    String title = name + "-" + m.getEffectiveName().replace(' ', '-');
    //name=""+name;
    Role staff = getStaffRole(o.getLabel());
    System.out.println("Nuovo ticket: " + title);
    if(g.getCategoriesByName(name, true).isEmpty()) {
      c = g.createCategory(name).complete();
      c.getManager().putRolePermissionOverride(g.getRolesByName("@everyone", true).get(0).getIdLong(),
              null,
              read).putRolePermissionOverride(staff.getIdLong(), read, null).queue();
    }
    else c = g.getCategoriesByName(name, true).get(0);
    TextChannel ticket = null;
    for (GuildChannel ch: c.getChannels()) {
      if(ch.getType() == ChannelType.TEXT) {
        TextChannel tc = (TextChannel) ch;
        if(tc.getName().contains(m.getEffectiveName()) || tc.getName().equalsIgnoreCase(title) || tc.getName().equalsIgnoreCase(name) || tc.getName().contains(
                m.getNickname())) ticket = tc;
      }
      System.out.println("Category: " + c + " Channel: " + ch + " Name: " + ch.getName());
    }
    if(ticket != null) return "Un ticket in questa categoria esiste già\n" + ticket.getAsMention();
    ticket = g.createTextChannel(title, c).complete();
    TextChannelManager manager = ticket.getManager();
    manager = manager.putMemberPermissionOverride(m.getIdLong(), read, null);
    manager = manager.putRolePermissionOverride(staff.getIdLong(), read, null);
    manager = manager.putRolePermissionOverride(g.getRolesByName("@everyone", true).get(0).getIdLong(), null, read);
    manager.queue();
    sendNewTicket(ticket, m, staff);
    System.out.println(o.getLabel() + " Staff: " + staff);
    return "Ticket created\n" + ticket.getAsMention();
  }
  
  private static Role getStaffRole(String type) {
    return switch(type.toLowerCase()) {
      case "store" -> getId(1300494552435523636L);
      case "candidatura" -> getId(1299775608318070805L);
      //case "candidatura" -> getId(1299774585914396682L);
      default -> getId(1299656548876029963L);
    };
  }
  
  public static void sendNewTicket(TextChannel ticket, Member m, Role staff) {
    EmbedBuilder b = new EmbedBuilder();
    b.setDescription(m.getAsMention() + "\n<:exlamation_mark:1301318181041864744> » Ti ricordiamo che il nostro Staff è composto da volontari, quindi ti preghiamo di avere pazienza e di non scrivere messaggi inutili." + staff.getAsMention() + "\n<:leftarrowyellow:1301576733216079872> Attenda un membro dello staff per assistenza<:staff:1301318147940421754> ");
    ticket.sendMessageEmbeds(b.build()).addActionRow(Button.success("claim", "Rivendica il ticket"),
            Button.danger("close", "chiudi il ticket")).queue();
    //ticket.sendMessage(staff.getAsMention()).queueAfter(30, TimeUnit.SECONDS, message->message.delete()/*.queue()*/);
  }
  
  private static Role getId(long id) {
    return ticket.getShardManager().getGuildById(1299516238686458058L).getRoleById(id);
  }
  
  /*private static Emoji getEmote(String name){
      //return VisionBot.bot.shardManager.getEmojiCache().getElementById(id);
      return VisionBot.bot.shardManager.getGuildById(1299516238686458058L).getEmojisByName(name,true).get(0);
 }*/
  public static void closeTicket(@NotNull GenericInteractionCreateEvent event) {
    if(event.getChannel() instanceof TextChannel t) {
      if(isAvailableTicket(t, event.getMember()) || isStaff(event.getMember())) {
        t.delete().queueAfter(5, TimeUnit.SECONDS, (q)->System.out.println("Chiuso un ticket\n"));
        if(event instanceof SlashCommandInteractionEvent e) e.reply("fatto").setEphemeral(true).queue();
        t.sendMessage(event.getUser().getAsMention() + " ha chiuso il ticket").queue();
        System.out.println("chiudo " + event.getChannel().getName());
      }
      else System.out.println("N/A");
    }
    else System.out.println("NaTC");
  }
  
  private static boolean isAvailableTicket(TextChannel t, Member m) {
    boolean send = false;
    for (PermissionOverride p: t.getPermissionOverrides()) {
      IPermissionHolder h = p.getPermissionHolder();
      if(h instanceof Role r) {
        if(m.getRoles().contains(r) && contSend(r, t)) {
          //System.out.println("per==, " + r.getPermissions() + " , " + r.getPermissions().contains(Permission.MESSAGE_SEND));
          send = true;
        }
        //System.out.println("nop==, " + r.getPermissions());
      }
      if(h instanceof Member me) {
        if(me.equals(m) && /*me.hasPermission(t, Permission.MESSAGE_SEND)*/contSend(me, t)) {
          send = true;
          //System.out.println("me==, " + me.getPermissions());
        }
        //System.out.println("non me==, " + me.getPermissions().toArray());
      }
    }
    //System.out.println(m.getEffectiveName() + " Send: " + send /*+ " dump " + t.getPermissionOverrides()*/);
    return isTicket(t) && isStaff(m) && send;
  }
  
  private static boolean contSend(Role r, TextChannel t) {
    for (Object o: r.getPermissions(t).toArray()) {
      if(o instanceof Permission p) {
        System.out.println(p);
        if(p.equals(Permission.MESSAGE_SEND)) return true;
      }
      else System.out.println(o.getClass());
    }
    return false;
  }
  
  private static boolean contSend(Member m, TextChannel t) {
    for (Object o: m.getPermissions(t).toArray()) {
      if(o instanceof Permission p) {
        System.out.println(p);
        if(p.equals(Permission.MESSAGE_SEND)) return true;
      }
      else System.out.println(o.getClass());
    }
    return false;
  }
  
  private static boolean isTicket(TextChannel c) {
    return c.getName().contains("ticket") && c.getIdLong() != 1299547580224634971L;
  }
  
  private static boolean isStaff(Member m) {
    return m.getRoles().contains(getStaffRole("base"));
  }
  
  public static void claimTicket(@NotNull GenericInteractionCreateEvent event) {
    System.out.println(event.getChannel() + " , " + event.getUser());
    if(event.getChannel() instanceof TextChannel t) {
      if(isStaff(event.getMember()) || isAvailableTicket(t, event.getMember())) {
        String type = t.getName().split("-")[1];
        Role role = getStaffRole(type);
        TextChannelManager m = t.getManager();
        m = m.putRolePermissionOverride(role.getIdLong(),
                EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY),
                EnumSet.of(Permission.MESSAGE_SEND, Permission.MESSAGE_MANAGE));
        m = m.putMemberPermissionOverride(event.getUser().getIdLong(), read, null);
        m.queue();
        //System.out.println(type+" , "+role);
        if(event instanceof SlashCommandInteractionEvent e) e.reply("fatto").setEphemeral(true).queue();
        t.sendMessage(event.getUser().getAsMention() + " ha reclamato il ticket").queue();
      }
      else System.out.println("N/A");
    }
    else System.out.println("NaTC");
  }
  
  public static void unclaimTicket(@NotNull GenericInteractionCreateEvent event) {
    if(event.getChannel() instanceof TextChannel t) {
      if(isAvailableTicket(t, event.getMember()) || isStaff(event.getMember())) {
        String type = t.getName().split("-")[1];
        Role role = getStaffRole(type);
        TextChannelManager m = t.getManager();
        m = m.putMemberPermissionOverride(event.getUser().getIdLong(), null, null);
        m = m.putRolePermissionOverride(role.getIdLong(), read, null);
        m.queue();
        if(event instanceof SlashCommandInteractionEvent e) e.reply("fatto").setEphemeral(true).queue();
        t.sendMessage(event.getUser().getAsMention() + " ha lasciato il controllo del ticket").queue();
      }
      else System.out.println("N/A");
    }
    else System.out.println("NaTC");
  }
  
  public static void addToTicket(@NotNull SlashCommandInteractionEvent event) {
    if(event.getChannel() instanceof TextChannel t) {
      if(isAvailableTicket(t, event.getMember()) || isStaff(event.getMember())) {
        User u = event.getOption("to").getAsUser();
        if(u == null) u = event.getUser();
        TextChannelManager m = t.getManager();
        m.putMemberPermissionOverride(event.getUser().getIdLong(), read, null);
        m.queue();
        event.reply(u.getAsMention() + " è stato aggiunto a questo ticket").queue();
      }
      else System.out.println("N/A");
    }
    else System.out.println("NaTC");
  }
  
  public static void transferTicket(@NotNull SlashCommandInteractionEvent event) {
    if(event.getChannel() instanceof TextChannel t) {
      if(isStaff(event.getMember()) || isAvailableTicket(t, event.getMember())) {
        User u = event.getOption("to").getAsUser();
        if(u == null) u = event.getUser();
        TextChannelManager m = t.getManager();
        m = m.putMemberPermissionOverride(event.getUser().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY), read);
        m = m.putMemberPermissionOverride(u.getIdLong(), read, null);
        m.queue();
        event.reply("Il ticket è stato trasferito a " + u.getAsMention()).queue();
      }
      else System.out.println("N/A");
    }
    else System.out.println("NaTC");
  }
  
  public static void updCatRole(SlashCommandInteractionEvent event) {
    Guild g = event.getGuild();
    String name = "Ticket-candidatura";
    Category c;
    if(g.getCategoriesByName(name, true).isEmpty()) {
      c = g.createCategory(name).complete();
    }
    else c = g.getCategoriesByName(name, true).get(0);
    updateRoles(c, "candidatura", g);
    name = "Ticket-store";
    if(g.getCategoriesByName(name, true).isEmpty()) {
      c = g.createCategory(name).complete();
    }
    else c = g.getCategoriesByName(name, true).get(0);
    updateRoles(c, "store", g);
    name = "Ticket-partnership";
    if(g.getCategoriesByName(name, true).isEmpty()) {
      c = g.createCategory(name).complete();
    }
    else c = g.getCategoriesByName(name, true).get(0);
    updateRoles(c, "partnership", g);
    name = "Ticket-segnalazione";
    if(g.getCategoriesByName(name, true).isEmpty()) {
      c = g.createCategory(name).complete();
    }
    else c = g.getCategoriesByName(name, true).get(0);
    updateRoles(c, "segnalazione", g);
    name = "Ticket-altro";
    if(g.getCategoriesByName(name, true).isEmpty()) {
      c = g.createCategory(name).complete();
    }
    else c = g.getCategoriesByName(name, true).get(0);
    updateRoles(c, "altro", g);
  }
  
  public static void updateRoles(Category c, String t, Guild g) {
    CategoryManager m = c.getManager();
    m = m.putRolePermissionOverride(g.getRolesByName("@everyone", true).get(0).getIdLong(), null, Collections.singleton(Permission.MESSAGE_SEND));
    m = m.putRolePermissionOverride(g.getRolesByName("@everyone", true).get(0).getIdLong(), null, Collections.singleton(Permission.VIEW_CHANNEL));
    m = m.putRolePermissionOverride(getStaffRole(t).getIdLong(), Collections.singleton(Permission.MESSAGE_SEND), null);
    m = m.putRolePermissionOverride(getStaffRole(t).getIdLong(), Collections.singleton(Permission.VIEW_CHANNEL), null);
    m = m.putRolePermissionOverride(getStaffRole(t).getIdLong(), Collections.singleton(Permission.MANAGE_CHANNEL), null);
  }
}