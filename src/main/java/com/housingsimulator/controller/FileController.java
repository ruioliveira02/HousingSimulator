package com.housingsimulator.controller;

import com.housingsimulator.annotations.API;
import com.housingsimulator.annotations.Endpoint;
import com.housingsimulator.model.AbstractEntity;
import com.housingsimulator.model.Model;
import com.housingsimulator.view.FileView;

import java.io.*;
import java.util.OptionalInt;

/**
 * Controller for file management
 */
@API
public class FileController extends Controller
{
    /**
     * Default constructor
     * @param model the model of the application
     */
    public FileController(Model model) {
        this.setModel(model);
        this.setView(new FileView());
    }

    /**
     * Gets the used view in the correct type
     * @return the view
     */
    @Override
    public FileView getView() {
        return (FileView) super.getView();
    }

    /**
     * Saves the state of the simulation in a file
     * @param fileName the name of the file to store in
     */
    @Endpoint(regex = "FILE SAVE (.+)")
    public void saveSimulationToFile(String fileName)
    {
        try {
            //CharSequence output = this.getModel().serializeData(new Serializer(true));
            //Files.writeString(Paths.get(fileName), output);
            FileOutputStream f = new FileOutputStream(fileName);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(this.getModel());
            o.close();
            f.close();
            this.getView().saveSuccess();
        }
        catch (IOException e) {
            this.getView().error(e.getMessage());
        }
    }

    /**
     * loads the state of the simulation from a file
     * @param fileName the file to load information from
     */
    @Endpoint(regex = "FILE LOAD (.+)")
    public void loadSimulationFromFile(String fileName)
    {
        try {
            //String input = Files.readString(Paths.get(fileName));
            //this.getModel().deserializeData(new Deserializer(), input);
            FileInputStream fi = new FileInputStream(fileName);
            ObjectInputStream oi = new ObjectInputStream(fi);
            this.getModel().copy((Model)oi.readObject());
            oi.close();
            fi.close();

            OptionalInt maxId = this.getModel().getSimulation().getEntityByType(AbstractEntity.class).stream().mapToInt(AbstractEntity::getId).max();
            if (maxId.isPresent())
                AbstractEntity.increaseCurrentId(maxId.getAsInt());

            this.getView().loadSuccess();
        }
        //catch (DeserializationException e) {
        //    this.getView().error(e.toString());
        //}
        catch (IOException | ClassNotFoundException e) {
            this.getView().error(e.getMessage());
        }
    }

    /**
     * executes multiple commands from a file
     * @param scriptName the file where the commands to execute are
     */
    @Endpoint(regex="SCRIPT EXEC (.+)")
    public String loadScript(String scriptName) {
        String result = "";
        try {
            FileInputStream fis = new FileInputStream(scriptName);
            result = new String(fis.readAllBytes());
            fis.close();
        } catch(IOException e) {
            this.getView().error("No such file or directory");
        }
        return result;
    }

    /**
     * sends help to the user
     */
    @Endpoint(regex="SEND HELP")
    public void sendHelp() {
        try {
            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("help.txt");
            this.getView().dumpString((new String(is.readAllBytes())));
            is.close();
        } catch(NullPointerException | IOException e) {
            this.getView().error("Could not find help file");
        }
    }
}
