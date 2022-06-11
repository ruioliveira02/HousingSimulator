package com.housingsimulator.view;


import java.util.Arrays;
import java.util.List;

public class SmartCameraView extends View {
    /**
     *  shows a camera as a view
     * @param id the if of the camera
     * @param cameraName the name of a camera
     * @param on the state of the camera
     * @param cameraResolution the resolution of the camera
     * @param fileResolution the size of the file it stores
     */
    public void show(int id, String cameraName, boolean on,
                            String cameraResolution,long fileResolution) {
        System.out.println("CAMERA INFO");
        System.out.println("=============================");
        System.out.println("Id: " + id);
        System.out.println("Name: " + cameraName);
        System.out.println("On: " + on);
        System.out.println("cameraResolution: "+cameraResolution);
        System.out.println("fileResolution: "+fileResolution);
    }

    /**
     * Shows a collection of cameras
     * @param n the number of cameras to show
     * @param id the ids of the cameras
     * @param cameraName the names of the cameras  «
     * @param on the state of the cameras
     * @param cameraResolution the resolution of the cameras
     * @param fileResolution the file size of the cameras
     */
    public void showAll(int n, List<Integer> id, List<String> cameraName, List<Boolean> on,
                             List<String> cameraResolution, List<Long> fileResolution) {

        System.out.println("CAMERAS INFO");
        System.out.println("===========================");
        System.out.println();

        List<String> headers = Arrays.asList("Id", "Name", "On", "cameraResolution", "fileResolution");

        TablePrinter.tablePrint(n, headers, id, cameraName, on, cameraResolution, fileResolution);
    }

}
