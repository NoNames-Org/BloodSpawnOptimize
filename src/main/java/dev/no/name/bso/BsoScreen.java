package dev.no.name.bso;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;

public class BsoScreen extends Screen {
    private static final int WIDTH = 220;
    private static final int ROW = 24;

    private final BsoConfig config;

    public BsoScreen(BsoConfig config) {
        super(Component.literal("BloodSpawnOptimize"));
        this.config = config;
    }

    @Override
    protected void init() {
        int x = (width - WIDTH) / 2;
        int y = Math.max(20, height / 2 - ROW * 4);
        addRenderableWidget(new StringWidget(x, y, WIDTH, 12, title, font));
        y += 20;
        addRenderableWidget(checkbox("Включено", config.enabled, x, y, value -> config.enabled = value));
        y += ROW;
        addRenderableWidget(new Slider(x, y, BsoConfig.MIN_DISTANCE, BsoConfig.MAX_DISTANCE, config.distance,
                value -> "Дистанция: " + Math.round(value) + " бл.", value -> config.distance = (int) Math.round(value)));
        y += ROW;
        addRenderableWidget(checkbox("Только во взгляде", config.frustum, x, y, value -> config.frustum = value));
        y += ROW;
        addRenderableWidget(new Slider(x, y, 0, BsoConfig.MAX_MARGIN, config.margin,
                value -> String.format("Запас коробки: %.2f", value), value -> config.margin = Math.round(value * 4) / 4.0));
        y += ROW;
        addRenderableWidget(checkbox("Текстовые дисплеи тоже", config.textDisplays, x, y, value -> config.textDisplays = value));
        y += ROW;
        addRenderableWidget(checkbox("Убрать тени дисплеев", config.hideShadows, x, y, value -> config.hideShadows = value));
        y += ROW + 6;
        addRenderableWidget(Button.builder(Component.literal("Готово"), button -> onClose()).bounds(x, y, WIDTH, 20).build());
    }

    private Checkbox checkbox(String label, boolean selected, int x, int y, BoolSetter setter) {
        return Checkbox.builder(Component.literal(label), font).pos(x, y).selected(selected)
                .onValueChange((box, value) -> setter.set(value)).build();
    }

    @Override
    public void onClose() {
        config.save();
        super.onClose();
    }

    private interface BoolSetter {
        void set(boolean value);
    }

    private static final class Slider extends AbstractSliderButton {
        private final double min;
        private final double max;
        private final DoubleFunction<String> label;
        private final DoubleConsumer setter;

        Slider(int x, int y, double min, double max, double current, DoubleFunction<String> label, DoubleConsumer setter) {
            super(x, y, WIDTH, 20, Component.empty(), (current - min) / (max - min));
            this.min = min;
            this.max = max;
            this.label = label;
            this.setter = setter;
            updateMessage();
        }

        private double real() {
            return min + (max - min) * value;
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.literal(label.apply(real())));
        }

        @Override
        protected void applyValue() {
            setter.accept(real());
        }
    }
}
