package com.housingsimulator.example;

import com.housingsimulator.serialization.Deserializer;
import com.housingsimulator.serialization.Serializer;
import com.housingsimulator.simulation.*;

public class Teste
{
    public static void main(String[] args) throws Exception
    {
        SmartHouse casa = new SmartHouse("Rua da Batata, Braga", new Lamp(10), new Lamp(20), new Lamp(30));
        Simulator simulator = new Simulator(casa);

        simulator.addEvent(new ToggleLampEvent(1, 10));
        //simulator.addEvent(new SetEntityEvent(0, 20, new ImmutableEntity<>("Rua da Cegonha, Porto")));
        simulator.addEvent(new CallMethodEvent(2, 70, "Set", true));

        simulator.removeEvents((Event e) -> e instanceof ToggleLampEvent);

        String s = new Serializer(true).serializeObj(simulator);
        System.out.println(s);
        Simulator aux = (Simulator) new Deserializer().deserializeObj(s).ans();

        SmartHouse casa2 = aux.getStateAs(SmartHouse.class, 100);
        System.out.println(casa2.consumoTotal());
    }
}