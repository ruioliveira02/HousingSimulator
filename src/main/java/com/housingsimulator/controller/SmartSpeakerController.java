package com.housingsimulator.controller;

import com.housingsimulator.annotations.API;
import com.housingsimulator.annotations.Endpoint;
import com.housingsimulator.controller.event.AddBrandEvent;
import com.housingsimulator.controller.event.AddDeviceEvent;
import com.housingsimulator.controller.event.UpdateBrandEvent;
import com.housingsimulator.controller.event.UpdateDeviceEvent;
import com.housingsimulator.exceptions.IllegalVolumeException;
import com.housingsimulator.exceptions.NoSuchEntityException;
import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.model.*;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.view.SmartSpeakerView;

import java.util.*;

/**
 * The controller for smart speakers / speaker brands
 */
@API
public class SmartSpeakerController extends Controller {

    /**
     * Default constructor
     * @param model The model of the application
     */
    public SmartSpeakerController(Model model) {
        this.setModel(model);
        this.setView(new SmartSpeakerView());
    }

    /**
     * Gets all the brands in the simulation
     */
    @Endpoint(regex="BRAND LIST ALL")
    public void getBrands() {
        Set<SpeakerBrand> brands = this.getModel().getSimulation().getAllSpeakerBrands();
        this.getView().showBrands(brands.size(), brands.stream().map(SpeakerBrand::getId).toList(),
                brands.stream().map(SpeakerBrand::getName).toList(),
                brands.stream().map(SpeakerBrand::getDailyConsumption).toList(),
                brands.stream().map(SpeakerBrand::getInstallationPrice).toList());
    }

    /**
     * Adds a speaker brand
     * @param name the name of the brand
     * @param dailyConsumption the daily consumption of the brand
     */
    @Endpoint(regex="BRAND ADD NAME=(.+) DAILY_CONSUMPTION=(\\d+) INSTALLATION_COST=(\\d+\\.?\\d*)")
    public void addBrand(String name, Integer dailyConsumption, Double installationCost) {
        SpeakerBrand brand = new SpeakerBrand(name, dailyConsumption, installationCost);
        this.getModel().addSimulatorEvent(new AddBrandEvent(this.getModel().getTime(), brand));
        this.getModel().updateState();
    }

    /**
     * Updates a barnd
     * @param oldName the old name of the brand
     * @param newName the new name of the brand
     * @param dailyConsumption the new daily consumption
     */
    @Endpoint(regex="BRAND UPDATE OLD_NAME=(.+) NEW_NAME=(.+) DAILY_CONSUMPTION=(\\d+) INSTALLATION_COST=(\\d+\\.?\\d*)")
    public void updateBrand(String oldName, String newName, Integer dailyConsumption, Double installationCost) {
        SpeakerBrand brand = new SpeakerBrand(newName, dailyConsumption, installationCost);
        this.getModel().addSimulatorEvent(new UpdateBrandEvent(this.getModel().getTime(), oldName, brand));
        this.getModel().updateState();
    }


    /**
     * Gets the speaker with the given id
     * @param id the given id
     */
    @Endpoint(regex="SPEAKER GET ID=(\\d+)")
    public void getSpeakerById(Integer id) {
        try {
            SmartSpeaker sp = this.getModel().getSimulation().getDevice(SmartSpeaker.class, id);
            this.getView().show(sp.getId(), sp.getName(), sp.getOn(), sp.getBrand().getName(),
                    sp.getBrand().getDailyConsumption(), sp.getVolume(), sp.getRadioStation(), sp.installationPrice());
        }catch (NoSuchEntityException | WrongEntityTypeException e){
            this.getView().warning(e.getMessage());
        }
    }


    /**
     * Gets all the speakers in a house
     * @param houseId the id of the house
     */
    @Endpoint(regex="SPEAKER GET HOUSE=(\\d+)")
    public void getSpeakersByHouse(Integer houseId) {
        try {
            Set<SmartSpeaker> speakers = this.getModel().getSimulation().getDevicesInHouse(SmartSpeaker.class,houseId);
            this.callViewOnCollection(speakers);
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
     * @param brandName the name of the brand
     * @param radioStation the radio station playing
     * @param volume the volume of the speaker
     */
    @Endpoint(regex="SPEAKER ADD HOUSE=(\\d+) ROOM=(.+) NAME=(.+) ON=(TRUE|FALSE) BRAND=(.+) STATION=(.+) VOLUME=(\\d+)")
    public void addSpeaker(Integer houseId, String room, String name, Boolean on, String brandName, String radioStation, Integer volume) {
        SpeakerBrand brand = this.getModel().getSimulation().getSpeakerBrand(brandName);

        if(brand == null) {
            this.getView().error("No brand named " + brandName);
        } else {
            Entity e = this.getModel().getSimulation().getEntity(houseId);

            if (!(e instanceof SmartHouse)){
                this.getView().warning("no such house with id: "+houseId);
            }
            else {
                try {
                    this.getModel().addSimulatorEvent(new AddDeviceEvent(this.getModel().getTime(), houseId, room,
                            new SmartSpeaker(on, name, volume, radioStation, brand)));
                    this.getModel().updateState();
                } catch(IllegalVolumeException ex) {
                    this.getView().error("Volume must be between 0 and 100");
                }

            }
        }
    }



    /**
     * Updates a speaker
     * @param id the id of speaker to update
     * @param name the name of the speaker
     * @param on whether the speaker is on
     * @param brandName the name of the brand
     * @param radioStation the radio station
     * @param volume the volume
     */
    @Endpoint(regex = "SPEAKER UPDATE ID=(\\d+) NAME=(.+) ON=(TRUE|FALSE) BRAND=(.+) STATION=(.+) VOLUME=(\\d+)")
    public void updateSpeaker(Integer id, String name, Boolean on, String brandName, String radioStation, Integer volume) {
        SpeakerBrand brand = this.getModel().getSimulation().getSpeakerBrand(brandName);

        if(brand == null) {
            this.getView().error("No brand named " + brandName);
        } else {
            Entity e = this.getModel().getSimulation().getEntity(id);

            if (!(e instanceof SmartSpeaker)){
                this.getView().warning("no such speaker with id: "+id);
            }
            else {
                try {
                    this.getModel().addSimulatorEvent(new UpdateDeviceEvent(this.getModel().getTime(), id,
                            new SmartSpeaker(on, name, volume, radioStation, brand)));
                    this.getModel().updateState();
                } catch(IllegalVolumeException ex) {
                    this.getView().error("Volume must be between 0 and 100");
                }

            }
        }
    }

    /**
     * Makes the view display the given collection of speakers
     *
     * @param speakers the collection of speakers
     */
    private void callViewOnCollection(Collection<SmartSpeaker> speakers) {
        List<Integer> ids = speakers.stream().map(SmartSpeaker::getId).toList();
        List<String> names = speakers.stream().map(SmartSpeaker::getName).toList();
        List<Boolean> on = speakers.stream().map(SmartSpeaker::getOn).toList();
        List<String> brands = speakers.stream().map(s->s.getBrand().getName()).toList();
        List<Integer> dailyConsumption = speakers.stream().map(s->s.getBrand().getDailyConsumption()).toList();
        List<Integer> volumes = speakers.stream().map(SmartSpeaker::getVolume).toList();
        List<String> stations = speakers.stream().map(SmartSpeaker::getRadioStation).toList();
        List<Double> installations = speakers.stream().map(SmartSpeaker::installationPrice).toList();

        this.getView().showAll(speakers.size(), ids, names, on, brands, dailyConsumption, volumes, stations, installations);
    }

    /**
     * Returns the view associated with the controller in the correct type
     * @return the view of the controller
     */
    @Override
    public SmartSpeakerView getView() {
        return (SmartSpeakerView)super.getView();
    }
}
