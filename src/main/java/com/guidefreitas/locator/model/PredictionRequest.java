/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guidefreitas.locator.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author guilherme
 */
public class PredictionRequest {

    private Map<String, Float> accessPointsData;
    
    public PredictionRequest(){
        this.accessPointsData = new HashMap<>();
    }
    
    public void addAccessPoint(String bsid, Float signal){
        this.accessPointsData.put(bsid, signal);
    }
    
    public Map<String, Float> getAccessPointData(){
        return this.accessPointsData;
    }
}
