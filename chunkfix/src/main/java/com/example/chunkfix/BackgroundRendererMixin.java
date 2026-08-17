package com.example.chunkfix.mixin;

import com.example.chunkfix.ChunkFixConfig;
import com.example.chunkfix.ChunkFixMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.render.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Inject(method = "applyFog", at = @At("RETURN"))
    private static void chunkfix$overrideFog(Camera camera, BackgroundRenderer.FogType fogType,
                                            float viewDistance, boolean thickFog, float tickDelta,
                                            CallbackInfo ci) {
        // Chỉ áp dụng cho fog địa hình, không áp dụng khi camera ở trong nước/lava
        if (fogType != BackgroundRenderer.FogType.FOG_TERRAIN) return;
        if (!camera.getSubmergedFluidState().isEmpty()) return;

        ChunkFixConfig config = ChunkFixMod.CONFIG;
        if (config == null || !config.enabled) return;

        int fogStart = config.getFogStart();
        int fogEnd = config.getFogEnd();

        // Đảm bảo hợp lệ
        if (fogEnd <= fogStart) fogEnd = fogStart + 1;

        // Ghi đè fog
        RenderSystem.setShaderFogStart(fogStart);
        RenderSystem.setShaderFogEnd(fogEnd);
    }
}
