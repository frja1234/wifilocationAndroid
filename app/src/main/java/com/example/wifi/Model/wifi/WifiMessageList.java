package com.example.wifi.Model.wifi;

public class WifiMessageList {

    private String id;

    private String name;

    private String level;

    private boolean isUse;

    public WifiMessageList(String id, String name, String level,boolean isUse){
        this.id =id;
        this.name = name;
        this.level = level;
        this.isUse = isUse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }

    @Override
    public String toString() {
        return "WifiMessageList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", isUse=" + isUse +
                '}';
    }
}
