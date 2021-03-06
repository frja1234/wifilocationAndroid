package com.example.wifi.Utils;

import java.util.UUID;

public class IdUtils {

    private String id;

    public String getId() {
        UUID uuid = UUID.randomUUID();
        id = uuid.toString().replace("-", "");
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
