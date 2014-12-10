/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guidefreitas.locator.config;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.guidefreitas.locator.model.AccessPoint;
import com.guidefreitas.locator.model.LocationData;
import com.guidefreitas.locator.model.Room;
import com.guidefreitas.locator.services.AbstractService;
import com.guidefreitas.locator.services.LocationService;
import com.guidefreitas.locator.services.RoomService;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme
 */
public class DatabaseManager {
    private static ConnectionSource connectionSource;
    
    public static void createDatabase(){
        try {
            String databaseUrl = Config.getDatabaseUrl();
            connectionSource = new JdbcConnectionSource(databaseUrl);
        
            Class[] models = new Class[]{ 
                Room.class,
                LocationData.class,
                AccessPoint.class
            };
            
            for(Class cls : models){
                if(!DaoManager.createDao(connectionSource, cls).isTableExists()){
                    TableUtils.createTable(connectionSource, cls);
                    TableUtils.clearTable(connectionSource, cls);
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AbstractService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void seedLocations(String dataFileName){
        String fileName = dataFileName;
        
        try {
            String databaseUrl = Config.getDatabaseUrl();
            connectionSource = new JdbcConnectionSource(databaseUrl);
            
            FileReader reader = new FileReader(fileName);
            BufferedReader buffer = new BufferedReader(reader);
            Object[] linhas =  buffer.lines().toArray();
            for(Object obj : linhas){
                try{
                    String linha = (String) obj;
                    LocationData locData = new LocationData();
                    String[] data = linha.split("\\,");
                    String roomName = data[0];
                    Room room = RoomService.getInstance().getByName(roomName);
                    if(room == null){
                       room = new Room();
                       room.setName(roomName);
                       RoomService.getInstance().create(room);
                    }
                    locData.setRoom(room);
                    locData.setUuid(data[1]);
                    String bssid = data[2];
                    AccessPoint ap = LocationService.getInstance().getAccessPointByBSSID(bssid);
                    if(ap == null){
                        ap = new AccessPoint();
                        ap.setBssid(bssid);
                        LocationService.getInstance().createAccessPoint(ap);
                    }
                    locData.setAccessPoint(ap);
                    locData.setNetworkName(data[3]);
                    locData.setSignalIntesity(Float.parseFloat(data[4]));
                    LocationService.getInstance().create(locData);
                }catch(Exception ex){
                    continue;
                }
            }
            
            System.out.println("Location count: " + LocationService.getInstance().getCount());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void seedDatabase(String dataFileName){
        try {
            String databaseUrl = Config.getDatabaseUrl();
            connectionSource = new JdbcConnectionSource(databaseUrl);
            
        } catch (SQLException ex) {
            Logger.getLogger(AbstractService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
