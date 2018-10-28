package com.example.reactive_primes;

import java.util.HashMap;

class Spectre {
    public Long index;
    public HashMap<String, Long> spectre;

    public Spectre(long index) {
        this.index = index;
    }

    public Spectre(long index, HashMap<String, Long> spectre) {
        this.index = index;
        this.spectre = spectre;
    }

    @Override
    public String toString() {
        String value = "";
        if(spectre != null) {
            value += String.join(",", spectre.keySet());
        }
        return "" + index + "[" + value + "]";
    }

}
