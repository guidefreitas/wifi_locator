/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guidefreitas.locator.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.guidefreitas.locator.model.AccessPoint;
import com.guidefreitas.locator.model.LocationData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme
 */
public class LocationService extends AbstractService{
    private static LocationService instance;
    private Dao<LocationData, Long> locationDao;
    private Dao<AccessPoint, Long> accesspointDao;
    
    
    public static LocationService getInstance(){
        if(instance == null){    
            instance  = new LocationService();
        }
        return instance;
    }
    public LocationService(){
        super();
        try {
            locationDao = DaoManager.createDao(connectionSource, LocationData.class);
            accesspointDao = DaoManager.createDao(connectionSource, AccessPoint.class);
        } catch (SQLException ex) {
            Logger.getLogger(LocationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void create(LocationData location) throws SQLException{
        this.locationDao.create(location);
    }
    
    public long getCount() throws SQLException{
        return this.locationDao.countOf();
    }
    
    public List<AccessPoint> getAccessPoints() throws SQLException{
        List<AccessPoint> aps = accesspointDao.queryForAll();
        return aps;
    }
    
    public AccessPoint getAccessPointByBSSID(String bssid) throws SQLException{
        AccessPoint ap = accesspointDao.queryBuilder()
                .orderBy(AccessPoint.BSSID_FIELD, true)
                .where()
                .eq(AccessPoint.BSSID_FIELD, bssid)
                .queryForFirst();
        
        return ap;
    }
    
    public void createAccessPoint(AccessPoint ap) throws SQLException{
        this.accesspointDao.create(ap);
    }
    
    public List<LocationData> getAllLocationData() throws SQLException {
        List<LocationData> data = locationDao.queryForAll();
        return data;
    }
    
    public LocationData getLocationDataByUUID(String uuid) throws SQLException{
        LocationData data = locationDao.queryBuilder()
                .where()
                .eq(LocationData.UUID_FIELD, uuid)
                .queryForFirst();
        
        return data;
    }
}
