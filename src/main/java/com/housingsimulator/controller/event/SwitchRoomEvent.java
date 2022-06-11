package com.housingsimulator.controller.event;

import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.*;

public class SwitchRoomEvent extends Event {

    private boolean switchOn;
    private int houseId;
    private String roomName;

    @SerializeIgnore
    private SmartHouse house;


    public SwitchRoomEvent() { }

    public SwitchRoomEvent(double time, int houseId, String roomName, boolean switchOn) {
        super(time);
        this.houseId = houseId;
        this.roomName = roomName;
        this.switchOn = switchOn;
    }

    public SwitchRoomEvent(SwitchRoomEvent e) {
        super(e);
        this.houseId = e.houseId;
        this.roomName = e.roomName;
        this.switchOn = e.switchOn;
        this.house = e.house;
    }


    @Override
    public Event clone() {
        return new SwitchRoomEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        this.house = (SmartHouse)state.getEntity(this.houseId);
        this.house.switchAllInRoom(this.roomName, this.switchOn);
        return Collections.singleton(this.houseId);
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        SmartHouse h = this.house.clone();
        h.advanceBy(time - this.getTime());
        return h;
    }
}

