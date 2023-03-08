package com.disruptor;

public class Event {

    private int uid;
    private int type;
    private String value;

    public Event() {
    }

    public Event(int uid, int type, String value) {
        this.uid = uid;
        this.type = type;
        this.value = value;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Event{" +
                "value='" + value + '\'' +
                '}';
    }
}
