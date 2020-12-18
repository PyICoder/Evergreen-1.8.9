/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.event.bus;

import net.evergreen.client.Evergreen;
import net.evergreen.client.exception.IllegalAnnotationException;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author isXander
 */
public class EventBus {

    private final Map<Object, List<EventMethod>> registeredClasses = new HashMap<>();

    public void register(Object o) {
        List<EventMethod> eventMethods = new ArrayList<>();

        for (Method m : o.getClass().getDeclaredMethods()) {
            SubscribeEvent annotation = m.getAnnotation(SubscribeEvent.class);

            if (annotation != null) {
                try {
                    Class<?> parameterType = m.getParameterTypes()[0];
                    if (parameterType == null) {
                        throw new IllegalAnnotationException("Annotated event method did not contain parameter for event specification.");
                    }

                    m.setAccessible(true);
                    eventMethods.add(new EventMethod(o, m, parameterType, annotation));
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    throw new ReportedException(CrashReport.makeCrashReport(e, "Event does not contain argument."));
                }
            }
        }

        eventMethods.sort(Comparator.comparingInt(em -> em.getAnnotation().priority().getValue()));
        registeredClasses.put(o, eventMethods);
    }

    public void unregister(Object instance) {
        registeredClasses.remove(instance);
    }
    
    public void post(@NotNull Object event) {
        registeredClasses.forEach((object, methodList) -> {
            for (EventMethod em : methodList) {
                if (em.getEvent().equals(event.getClass())) {
                    try {
                        Minecraft.getMinecraft().mcProfiler.startSection(em.getMethod().getName());
                        em.getMethod().invoke(em.getInstance(), event);
                        Minecraft.getMinecraft().mcProfiler.endSection();
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
