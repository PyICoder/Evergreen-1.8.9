/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.mixins.inject;

import net.evergreen.client.command.ClientCommandHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.spongepowered.asm.mixin.*;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Shadow @Final public NetHandlerPlayClient sendQueue;

    /**
     * Place where all messages are sent, inject here for {@link ClientCommandHandler}
     * @param msg message to be send
     * @author isXander
     */
    @Overwrite
    public void sendChatMessage(String msg) {
        if (ClientCommandHandler.instance.executeCommand(msg) != 0) return;
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(msg));
    }

}
