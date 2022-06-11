package com.housingsimulator.controller.event;

import com.housingsimulator.model.Simulation;
import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class MoveDeviceEvent extends Event {
    private int deviceId;
    private int houseId;
    private String roomName;

    @SerializeIgnore
    private SmartHouse oldHouse;
    @SerializeIgnore
    private SmartHouse newHouse;


    public MoveDeviceEvent() { }

    public MoveDeviceEvent(double time, int deviceId, int houseId, String roomName) {
        super(time);
        this.deviceId = deviceId;
        this.houseId = houseId;
        this.roomName = roomName;
    }

    public MoveDeviceEvent(MoveDeviceEvent e) {
        super(e);
        this.deviceId = e.deviceId;
        this.houseId = e.houseId;
        this.roomName = e.roomName;
        this.oldHouse = e.oldHouse;
        this.newHouse = e.newHouse;
    }


    @Override
    public Event clone() {
        return new MoveDeviceEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        Simulation sim = new Simulation(state);

        for (SmartHouse house : sim.getHouses()) {
            SmartDevice device = house.getDevices().get(this.deviceId);
            if (device != null)
            {
                if (this.houseId == house.getId()) {
                    house.moveToRoom(deviceId, roomName);
                    this.newHouse = house;
                } else {
                    house.removeDevice(device);
                    this.oldHouse = house;

                    sim.addDevice(houseId, roomName, device);
                    this.newHouse = sim.getHouses().stream().filter(h -> h.getId() == houseId).findAny().get();
                }

                Set<Integer> ans = new TreeSet<>();
                ans.add(house.getId());
                ans.add(this.houseId);
                return ans;
            }
        }

        throw new IllegalStateException("No device with id " + deviceId + " was found");
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        SmartHouse h;

        if (entity_index == this.houseId)
            h = this.newHouse.clone();
        else
            h = this.oldHouse.clone();

        h.advanceBy(time - this.getTime());
        return h;
    }
}
