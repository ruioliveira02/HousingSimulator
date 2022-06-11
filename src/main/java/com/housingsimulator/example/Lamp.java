package com.housingsimulator.example;

import com.housingsimulator.serialization.AutoSerializable;
import com.housingsimulator.serialization.DeserializationException;
import com.housingsimulator.serialization.Deserializer;
import com.housingsimulator.serialization.Serializer;
import com.housingsimulator.simulation.*;
import com.sun.source.tree.Tree;

import java.util.Map;
import java.util.TreeMap;

public class Lamp implements Entity, AutoSerializable
{
    boolean ligado;
    double potencia;
    double consumo;

    public void Set(Boolean ligado) {
        this.ligado = ligado;
    }

    public Lamp() {}

    public Lamp(int potencia) {
        this.ligado = false;
        this.potencia = potencia;
        this.consumo = 0;
    }

    public Lamp(Lamp l) {
        this.ligado = l.ligado;
        this.potencia = l.potencia;
        this.consumo = l.consumo;
    }

    public Lamp clone() {
        return new Lamp(this);
    }

    public void advanceBy(double time)
    {
        if (this.ligado)
            this.consumo += this.potencia * time;
    }
}