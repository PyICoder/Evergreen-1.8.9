package net.evergreen.client.mixins.impl;

import cc.hyperium.event.Phase;
import net.evergreen.client.event.EventClientTick;
import net.evergreen.client.event.EventRenderTick;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow @Final public Profiler mcProfiler;
    @Shadow private Timer timer;

    /**
     * Injects the pre render event
     *
     * @author isXander
     */
    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private void injectRenderTickPre(CallbackInfo ci) {
        this.mcProfiler.endStartSection("modGameRenderPre");
        new EventRenderTick(Phase.PRE, this.timer.renderPartialTicks).post();
    }

    /**
     * Injects the post render event
     *
     * @author isXander
     */
    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", shift = At.Shift.AFTER))
    private void injectRenderTickPost(CallbackInfo ci) {
        this.mcProfiler.startSection("modGameRenderPost");
        new EventRenderTick(Phase.POST, this.timer.renderPartialTicks).post();
    }

    /**
     * Injects the pre tick event
     *
     * @author isXander
     */
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private void injectClientTickPre(CallbackInfo ci) {
        this.mcProfiler.startSection("modClientTickPre");
        new EventClientTick(Phase.PRE).post();
        this.mcProfiler.endSection();
    }

    /**
     * Injects the post tick event
     *
     * @author isXander
     */
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", shift = At.Shift.BEFORE))
    private void injectClientTickPost(CallbackInfo ci) {
        this.mcProfiler.endStartSection("modClientTickPost");
        new EventClientTick(Phase.POST).post();
    }

}
