package com.example.chunkfix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChunkFixConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("chunkfix.json");

    public boolean enabled = true;
    public int blurChunks = 3; // Số chunk nhìn rõ, phần cuối chunk này sẽ bị mờ

    public static ChunkFixConfig load() {
        ChunkFixConfig config = new ChunkFixConfig();
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                config = GSON.fromJson(json, ChunkFixConfig.class);
                // Kiểm tra giá trị hợp lệ
                if (config.blurChunks < 1) config.blurChunks = 1;
                if (config.blurChunks > 64) config.blurChunks = 64; // giới hạn hợp lý
            } catch (IOException | JsonSyntaxException e) {
                ChunkFixMod.LOGGER.error("Could not read config, using defaults", e);
                config = new ChunkFixConfig();
                save(config);
            }
        } else {
            save(config); // tạo file mặc định
        }
        return config;
    }

    public static void save(ChunkFixConfig config) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(config));
        } catch (IOException e) {
            ChunkFixMod.LOGGER.error("Could not save config", e);
        }
    }

    /**
     * Tính khoảng cách bắt đầu sương mù (block).
     * Công thức: start = n * 16 - thickness
     */
    public int getFogStart() {
        int thickness = getBlurThickness();
        return Math.max(0, blurChunks * 16 - thickness);
    }

    /**
     * Tính khoảng cách kết thúc sương mù (block).
     * Kết thúc tại ranh giới chunk thứ n.
     */
    public int getFogEnd() {
        return blurChunks * 16;
    }

    /**
     * Độ dày vùng mờ (block): (n - 2) * 10, tối thiểu 0.
     */
    public int getBlurThickness() {
        return Math.max(0, (blurChunks - 2) * 10);
    }
}
