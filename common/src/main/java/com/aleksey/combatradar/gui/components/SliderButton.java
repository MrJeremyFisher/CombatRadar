package com.aleksey.combatradar.gui.components;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.text.DecimalFormat;

/**
 * @author Aleksey Terzi
 */
public class SliderButton extends AbstractSliderButton {
    private static final DecimalFormat _decimalFormat = new DecimalFormat("#.##");

    private float _value;
    private final float _minValue;
    private final float _maxValue;
    private final String _name;
    private final boolean _integer;

    public float getValue() {
        return _value;
    }

    public SliderButton(int x, int y, int width, float maxValue, float minValue, String name, float value, boolean integer) {
        super(x, y, width, 20, Component.literal(name), (value - minValue) / (maxValue - minValue));

        _maxValue = maxValue;
        _minValue = minValue;
        _value = value;
        _name = name;
        _integer = integer;

        updateMessage();
    }

    @Override
    public void applyValue() {
        _value = _integer ? (float) Mth.floor(Mth.clampedLerp(this._minValue, this._maxValue, this.value)) : (float) Mth.clampedLerp(this._minValue, this._maxValue, this.value);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format("%s: %s", _name, _decimalFormat.format(_value))));
    }

    public void onClick(double d, double e) {
    }

    public void onRelease(double d, double e) {
    }
}