package com.housingsimulator.model;

import java.io.Serializable;

public record Installation(String deviceName, String room, double price) implements Serializable { }