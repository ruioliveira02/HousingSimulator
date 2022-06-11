package com.housingsimulator.example;

import com.housingsimulator.serialization.*;
import com.housingsimulator.simulation.*;

import java.text.CollationElementIterator;
import java.util.*;

public class SmartHouse implements SimState, AutoSerializable {
    private String address;

    @SimEntity(index=1)
    public Lamp l1;

    @SimEntity(index=2)
    public Lamp l2;

    @SimEntity(index=3)
    public Lamp l3;

    //@SimEntity(index=0)
    public void SetAddress(ImmutableEntity<String> e) {
        this.address = e.getObj();
    }

    //para getStateAs (usa um construtor refletido)
    public SmartHouse() { }

    public SmartHouse(String address, Lamp l1, Lamp l2, Lamp l3) {
        this.address = address;
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
    }

    @Override
    public Entity getEntity(int index) {
        return switch (index) {
            case 0 -> new ImmutableEntity<>(address);
            case 1 -> l1;
            case 2 -> l2;
            case 3 -> l3;
            default -> null;
        };
    }

    @Override
    public Set<Integer> getValidIds() {
        return new TreeSet<>(Arrays.asList(1, 2, 3));
    }

    public double consumoTotal() {
        return l1.consumo + l2.consumo + l3.consumo;
    }

    @Override
    public CharSequence serializeData(Serializer s) {
        return s.serializeCollection(new ArrayList<>(Arrays.asList(l1, l2, l3)));
    }

    @Override
    public int deserializeData(Deserializer d, String s) throws DeserializationException {
        DeserializationResult<? extends Collection<?>> ans = d.deserializeCollection(s);
        List<Object> list = new ArrayList<>(ans.ans());
        //this.address = (String)list.get(0);
        this.l1 = (Lamp)list.get(0);
        this.l2 = (Lamp)list.get(1);
        this.l3 = (Lamp)list.get(2);
        return ans.consumedLength();
    }
}
