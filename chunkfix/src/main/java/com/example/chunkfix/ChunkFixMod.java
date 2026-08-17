package com.example.chunkfix;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChunkFixMod implements ClientModInitializer {
    public static final String MOD_ID = "chunkfix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ChunkFixConfig CONFIG;

    @Override
    public void onInitializeClient() {
        LOGGER.info("ChunkFix client initializing...");
        CONFIG = ChunkFixConfig.load();
        // Không cần reload thường xuyên, chỉ tải một lần.
        // Có thể thêm lệnh reload nếu muốn.
    }
}
