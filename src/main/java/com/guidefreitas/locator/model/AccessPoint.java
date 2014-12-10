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

@DatabaseTable(tableName = "accesspoints")
public class AccessPoint{
    public static final String BSSID_FIELD = "bssid";
    
    @DatabaseField(generatedId = true)
    private Long id;
    
    @DatabaseField(canBeNull = false, unique = true, columnName = BSSID_FIELD)
    private String bssid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }
    
    
    
}
