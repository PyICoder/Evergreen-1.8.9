/*
 * Copyright [2020] [Evergreen]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.evergreen.client.discord;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import net.evergreen.client.Evergreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.OffsetDateTime;

public class EvergreenRPC {

    private static final Logger logger = LogManager.getLogger("Discord RPC");

    public static boolean isConnected = false;
    RichPresence.Builder builder = new RichPresence.Builder();
    IPCClient client = new IPCClient(784773930355261470L);

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * @author Hot Tutorials | Hot Tutorials#8262
     */
    public void init() {
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                setPresence("Starting Client...", "Starting Client...");
                client.sendRichPresence(builder.build());
            }
        });
        try {
            client.connect();
            builder.setStartTimestamp(OffsetDateTime.now());
            client.sendRichPresence(builder.build());
            logger.info("Connected to RPC Client!");
            this.setConnected(true);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.setConnected(false);
        }
        if (isConnected)
            logger.info("Connected to Discord RPC");
        else
            logger.info("Failed to connect to Discord RPC");
    }


    /**
     * @param firstLine  is the firstLine
     * @param secondLine is the secondLine
     * @author Hot Tutorials | Hot Tutorials#8262
     */
    public void setPresence(String firstLine, String secondLine) {
        builder.setDetails(firstLine)
                .setState(secondLine);
        client.sendRichPresence(builder.build());
    }

    /**
     * @param firstLine  is the firstLine
     * @param secondLine is the secondLine
     * @param largeImage is the large image that shows
     * @author Hot Tutorials | Hot Tutorials#8262
     */
    public void setPresence(String firstLine, String secondLine, String largeImage) {
        builder.setDetails(firstLine)
                .setState(secondLine)
                .setLargeImage(largeImage);
        client.sendRichPresence(builder.build());
    }

    /**
     * @param firstLine  is the firstLine
     * @param secondLine is the secondLine
     * @param largeImage is the large image that shows
     * @param smallImage is the image that shows in the bottom right of the largeImage
     * @author Hot Tutorials | Hot Tutorials#8262
     * @since b0.1
     */
    public void setPresence(String firstLine, String secondLine, String largeImage, String smallImage) {
        builder.setDetails(firstLine)
                .setState(secondLine)
                .setLargeImage(largeImage)
                .setSmallImage(smallImage);
        client.sendRichPresence(builder.build());
    }

}
