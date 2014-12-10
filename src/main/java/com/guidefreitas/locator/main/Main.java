/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guidefreitas.locator.main;

import com.guidefreitas.locator.config.DatabaseManager;
import com.guidefreitas.locator.services.PredictionService;
import weka.classifiers.Evaluation;

/**
 *
 * @author guilherme
 */
public class Main {
    public static void main(String[] args) throws Exception{
        //String dataFile = "wifi_data.txt";
        String dataFile = "wifi_data_university.txt";
        
        DatabaseManager.createDatabase();
        DatabaseManager.seedLocations(dataFile);
        PredictionService predService = PredictionService.getInstance();
        predService.generateTrainData();
        Evaluation eval = predService.train();
        System.out.println("================================================");
        System.out.println(eval.toSummaryString("\nResults\n======\n", true));
        System.out.println("================================================");
        System.out.println(eval.toMatrixString("\nConfusion Matrix\n======\n"));
        System.out.println("================================================");
    }
}
