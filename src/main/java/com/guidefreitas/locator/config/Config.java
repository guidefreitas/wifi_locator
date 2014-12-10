/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guidefreitas.locator.config;

/**
 *
 * @author guilherme
 */
public class Config {
    private static String databaseUrl = "jdbc:h2:mem:account";
    
    public static String getDatabaseUrl(){
        return databaseUrl;
    }
}
