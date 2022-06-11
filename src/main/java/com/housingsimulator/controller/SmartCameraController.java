package com.housingsimulator.controller;

import com.housingsimulator.annotations.API;
import com.housingsimulator.annotations.Endpoint;
import com.housingsimulator.controller.event.AddDeviceEvent;
import com.housingsimulator.controller.event.UpdateDeviceEvent;
import com.housingsimulator.exceptions.NoSuchEntityException;
import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.model.*;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.view.SmartCameraView;

import java.util.*;

@API
public class SmartCameraController extends Controller{

    /**
     * converts a camera resolution to a string
     * @param cr the camera resolution
     * @return the string
     */
    private static String cr2String(int[] cr){
        return cr[0]+"*"+cr[1];
    }



    /**
     * Default constructor
     * @param model The model of the application
     */
    public SmartCameraController(Model model) {
        this.setModel(model);
        this.setView(new SmartCameraView());
    }


    /**
     * Gets the camera with the given id
     * @param id the given id
     */
    @Endpoint(regex="CAMERA GET ID=(\\d+)")
    public void getCameraById(Integer id) {
        try {
            SmartCamera camera = this.getModel().getSimulation().getDevice(SmartCamera.class, id);
            int[] cr = camera.getCameraResolution();
            this.getView().show(camera.getId(), camera.getName(),camera.getOn(),cr2String(camera.getCameraResolution()),
                    camera.getFileResolution());

        }catch (NoSuchEntityException | WrongEntityTypeException e){
            this.getView().warning(e.getMessage());
        }
    }



    /**
     * Gets all the cameras in a house
     * @param houseId the id of the house
     */
    @Endpoint(regex="CAMERA GET HOUSE=(\\d+)")
    public void getCamerasByHouse(Integer houseId) {
        try {
            Set<SmartCamera> cameras = this.getModel().getSimulation().getDevicesInHouse(SmartCamera.class,houseId);
            this.callViewOnCollection(cameras);
        }catch (NoSuchEntityException | WrongEntityTypeException e){
            this.getView().warning(e.getMessage());
        }
    }



    /**
     * Adds a speaker to the simulation
     * @param houseId the id of the house to put the speaker in
     * @param room the room of the house to put the speaker in
     * @param name the name of the speaker
     * @param on whether the speaker is on
     * @param cameraX the resolution of the camera (X axis)
     * @param cameraY the resolution of the camera (Y axis)
     * @param file the resolution of the files stored
     */
    @Endpoint(regex="CAMERA ADD HOUSE=(\\d+) ROOM=(.+) NAME=(.+) ON=(TRUE|FALSE) CAMERARESOLUTION=\\((\\d+),(\\d+)\\) FILERESOLUTION=(\\d+)")
    public void addCamera(Integer houseId, String room, String name, Boolean on, Integer cameraX, Integer cameraY,
                           Long file) {
        Entity e = this.getModel().getSimulation().getEntity(houseId);

        if (!(e instanceof SmartHouse)){
            this.getView().warning("no such house with id: "+houseId);
        }
        else {
            this.getModel().addSimulatorEvent(new AddDeviceEvent(this.getModel().getTime(), houseId, room,
                    new SmartCamera(on, name, new int[]{cameraX, cameraY}, file)));
            this.getModel().updateState();
        }
    }

    /**
     * Updates a speaker
     * @param id the id of speaker to update
     * @param name the name of the speaker
     * @param on whether the speaker is on
     * @param cameraX the resolution of the camera (X axis)
     * @param cameraY the resolution of the camera (Y axis)
     * @param file the resolution of the files stored
     */
    @Endpoint(regex = "CAMERA UPDATE ID=(\\d+) NAME=(.+) ON=(TRUE|FALSE) CAMERARESOLUTION=\\((\\d+),(\\d+)\\) FILERESOLUTION=(\\d+)")
    public void updateCamera(Integer id, String name, Boolean on, Integer cameraX, Integer cameraY,
                             Integer file) {
        Entity e = this.getModel().getSimulation().getEntity(id);

        if (!(e instanceof SmartCamera)){
            this.getView().warning("no such camera with id: "+id);
        }
        else {
            this.getModel().addSimulatorEvent(new UpdateDeviceEvent(this.getModel().getTime(), id,
                    new SmartCamera(on, name, new int[]{cameraX, cameraY}, file)));
            this.getModel().updateState();
        }
    }


    /**
     * Calls the view on a list of cameras
     * @param cameras the cameras to print
     */
    private void callViewOnCollection(Collection<SmartCamera> cameras) {
        List<Integer> ids = cameras.stream().map(SmartCamera::getId).toList();
        List<String> names = cameras.stream().map(SmartCamera::getName).toList();
        List<Boolean> ons = cameras.stream().map(SmartCamera::getOn).toList();
        List<String> resolution = cameras.stream().map(c->cr2String(c.getCameraResolution())).toList();
        List<Long> fileResolution = cameras.stream().map(SmartCamera::getFileResolution).toList();

        this.getView().showAll(cameras.size(), ids, names, ons, resolution,fileResolution);
    }

    /**
     * Returns the view associated with the controller in the correct type
     * @return the view of the controller
     */
    @Override
    public SmartCameraView getView() {
        return (SmartCameraView)super.getView();
    }

}
