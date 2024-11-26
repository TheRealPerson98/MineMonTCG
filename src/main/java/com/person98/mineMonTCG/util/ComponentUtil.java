package com.person98.mineMonTCG.util;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ComponentUtil {

    public static @NotNull Component removeItalicsIfNotFound(@NotNull Component component) {
        if (component.children().isEmpty()) {
            if (!component.hasDecoration(TextDecoration.ITALIC)) {
                return component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
            }

            return component;
        }

        final List<Component> children = Lists.newArrayList();

        for (final Component child : component.children()) {
            children.add(removeItalicsIfNotFound(child));
        }

        return component.children(children);
    }


    public static @NotNull List<Component> removeItalicsIfNotFound(@NotNull List<Component> components) {
        final List<Component> newComponents = Lists.newArrayList();

        for (final Component component : components) {
            newComponents.add(removeItalicsIfNotFound(component));
        }

        return newComponents;
    }

    public static @NotNull Component toMiniMessage(@NotNull String literal) {
        return MiniMessage.miniMessage().deserialize(literal).decoration(TextDecoration.ITALIC, false);
    }

    public static @NotNull List<Component> toMiniMessage(@NotNull List<String> lines) {
        final List<Component> components = Lists.newArrayList();

        for (final String line : lines) {
            components.add(toMiniMessage(line));
        }

        return components;
    }
}
