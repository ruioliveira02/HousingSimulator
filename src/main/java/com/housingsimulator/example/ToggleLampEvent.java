package com.housingsimulator.example;

import com.housingsimulator.simulation.*;

import java.util.Collections;
import java.util.Set;

public class ToggleLampEvent extends Event
{
    private int lamp_index;
    private Lamp new_lamp;


    public ToggleLampEvent() { }

    public ToggleLampEvent(int lamp_index, double time) {
        super(time);
        this.lamp_index = lamp_index;
    }

    public ToggleLampEvent(ToggleLampEvent e) {
        super(e);
        this.lamp_index = e.lamp_index;
    }

    public ToggleLampEvent clone() {
        return new ToggleLampEvent(this);
    }

    public Set<Integer> init(Simulator.State state)
    {
        new_lamp = (Lamp)state.getEntity(lamp_index);
        new_lamp.ligado = !new_lamp.ligado;

        return Collections.singleton(lamp_index);
    }

    public Entity getEntity(int entity_index, double time)
    {
        Entity ans = new_lamp.clone();
        ans.advanceBy(time - this.getTime());
        return ans;
    }
}
