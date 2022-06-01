package com.cgreen.pitchconverter.datastore.pitch;

public class Rest extends MusicSymbol {
    Rest() { }

    @Override
    public char getPitchClass() {
        return 'r';
    }

    @Override
    public String getRegister() {
        return "N/A";
    }

    @Override
    public String toString() {
        return "[rest]";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null && this.getClass() == obj.getClass());
    }
}
