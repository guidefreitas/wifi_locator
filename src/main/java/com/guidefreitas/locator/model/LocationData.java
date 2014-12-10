/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guidefreitas.locator.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author guilherme
 */
@DatabaseTable(tableName = "locations_data")
public class LocationData {
    
    public static final String ROOM_FIELD = "room"; 
    public static final String UUID_FIELD = "uuid"; 
    public static final String AP_FIELD = "ap_id"; 
    public static final String NETWORK_NAME_FIELD = "network_name";
    public static final String SIGNAL_FIELD = "signal";
    
    @DatabaseField(generatedId = true)
    private Long id;
    
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh=true, columnName = ROOM_FIELD)
    private Room room;
    
    @DatabaseField(canBeNull = false, columnName = UUID_FIELD)
    private String uuid;
   
    @DatabaseField(canBeNull = false, foreign = true, columnName = AP_FIELD)
    private AccessPoint accessPoint;
    
    @DatabaseField(canBeNull = false, columnName = NETWORK_NAME_FIELD)
    private String networkName;
    
    @DatabaseField(canBeNull = false, columnName = SIGNAL_FIELD)
    private Float signalIntesity;
    
    public LocationData(){
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public AccessPoint getAccessPoint() {
        return this.accessPoint;
    }

    public void setAccessPoint(AccessPoint ap) {
        this.accessPoint = ap;
    }

    public Float getSignalIntesity() {
        return signalIntesity;
    }

    public void setSignalIntesity(Float signalIntesity) {
        this.signalIntesity = signalIntesity;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
}
