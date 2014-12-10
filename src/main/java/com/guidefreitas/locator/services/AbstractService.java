/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guidefreitas.locator.services;

import com.guidefreitas.locator.config.Config;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author guilherme
 */
public abstract class AbstractService {
    
    protected ConnectionSource connectionSource;
    
    public AbstractService(){
        try {
            String databaseUrl = Config.getDatabaseUrl();
            connectionSource = new JdbcConnectionSource(databaseUrl);
        } catch (SQLException ex) {
            Logger.getLogger(AbstractService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
