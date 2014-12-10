/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guidefreitas.locator.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.guidefreitas.locator.model.Room;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme
 */
public class RoomService extends AbstractService {
    private static RoomService instance;
    private Dao<Room, Long> roomDao;
    
    public static RoomService getInstance(){
        if(instance == null){    
            instance  = new RoomService();
        }
        return instance;
    }
    public RoomService(){
        super();
        try {
            roomDao = DaoManager.createDao(connectionSource, Room.class);
        } catch (SQLException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Room> getAll() throws SQLException{
        List<Room> rooms = roomDao.queryBuilder()
                .orderBy(Room.NAME_FIELD, true)
                .query();
        return rooms;
    }
    
    public Room getById(Long id) throws SQLException{
        Room room = roomDao.queryForId(id);
        return room;
    }
    
    public Room getByName(String name) throws SQLException{
        Room room = roomDao.queryBuilder()
                .where()
                .eq(Room.NAME_FIELD, name)
                .queryForFirst();
        
        return room;
    }
    
    public void create(Room room) throws SQLException{
        roomDao.create(room);
    }
   
}
