package org.matita08.VisionMC;

import static org.matita08.VisionMC.Settings.*;

public class VisionBots {
    public VisionBots() {
        ticket = new VisionTicket();
        staff = new VisionStaff();
    }

    public static void main(String[] args) {
        bots = new VisionBots();
    }
}
