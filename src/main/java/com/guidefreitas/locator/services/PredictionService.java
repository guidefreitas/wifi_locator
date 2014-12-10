/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guidefreitas.locator.services;

import com.guidefreitas.locator.model.AccessPoint;
import com.guidefreitas.locator.model.LocationData;
import com.guidefreitas.locator.model.PredictionRequest;
import com.guidefreitas.locator.model.Room;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;
/**
 *
 * @author guilherme
 */
public class PredictionService {
    private static PredictionService instance;
    private LibSVM classifier;
    
    public static PredictionService getInstance(){
        if(instance == null){
            instance = new PredictionService();
        }
        
        return instance;
    }
    
    private PredictionService(){
        
    }
    
    public String generateTestData(PredictionRequest req){
        try{
            StringBuilder sb = new StringBuilder();
            List<AccessPoint> aps = LocationService.getInstance().getAccessPoints();
            sb.append("@RELATION wifi\n");
            sb.append("\n");
            for(AccessPoint ap : aps){
                sb.append("@ATTRIBUTE ap" + ap.getId() + " REAL\n");
            }
            List<Room> rooms = RoomService.getInstance().getAll();
            sb.append("@ATTRIBUTE class {");
            for(int i=0;i<rooms.size();i++){
                Room room = rooms.get(i);
                sb.append(room.getId());
                if(i != rooms.size()-1)
                    sb.append(",");
            }
            sb.append(",none");
            sb.append("}\n");
            sb.append("\n");
            sb.append("@DATA\n");
            
            
            
            for (int c = 0; c < aps.size(); c++) {
                AccessPoint ap = aps.get(c);
                Map<String, Float> coletaData = req.getAccessPointData();
                if (coletaData.containsKey(ap.getBssid())) {
                    sb.append(coletaData.get(ap.getBssid()));
                } else {
                    sb.append("0");
                }
                if (c != aps.size() - 1) {
                    sb.append(",");
                } else {
                    sb.append(",none");
                    sb.append("\n");
                }
            }
            
            return sb.toString();
            
        }catch(Exception ex){
            Logger.getLogger(PredictionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public String generateTrainData(){
        try{
            StringBuilder sb = new StringBuilder();
            List<AccessPoint> aps = LocationService.getInstance().getAccessPoints();
            sb.append("@RELATION wifi\n");
            sb.append("\n");
            for(AccessPoint ap : aps){
                sb.append("@ATTRIBUTE ap" + ap.getId() + " REAL\n");
            }
            List<Room> rooms = RoomService.getInstance().getAll();
            sb.append("@ATTRIBUTE class {");
            for(int i=0;i<rooms.size();i++){
                Room room = rooms.get(i);
                sb.append(room.getId());
                if(i != rooms.size()-1)
                    sb.append(",");
            }
            sb.append(",none");
            sb.append("}\n");
            sb.append("\n");
            sb.append("@DATA\n");
            List<LocationData> locData = LocationService.getInstance().getAllLocationData();

            HashMap<String, HashMap<Long, Float>> data = new HashMap<String, HashMap<Long,Float>>();
            
            for(LocationData loc : locData){
                String uuid = loc.getUuid();
                for(AccessPoint ap : aps){
                    if(loc.getAccessPoint().getId().equals(ap.getId())){
                        if(!data.containsKey(uuid)){
                            data.put(uuid, new HashMap<Long, Float>());    
                        }
                        data.get(uuid).put(ap.getId(), loc.getSignalIntesity());
                    }
                    
                }
            }
            
            
            for(String key : data.keySet()){
                for(int c=0;c<aps.size();c++){
                    AccessPoint ap = aps.get(c);
                    Map<Long, Float> coletaData = data.get(key);
                    if(coletaData.containsKey(ap.getId())){
                        sb.append(coletaData.get(ap.getId()));
                    }else{
                        sb.append("0");
                    }
                    if(c!=aps.size()-1){
                        sb.append(",");
                    }else{
                        String category = LocationService.getInstance().getLocationDataByUUID(key).getRoom().getId().toString();
                        sb.append("," + category);
                        sb.append("\n");
                    }
                }
            }
            
            return sb.toString();
            
        }catch(Exception ex){
            Logger.getLogger(PredictionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public Evaluation train(){
        try{
            String arffData = this.generateTrainData();
            InputStream stream = new ByteArrayInputStream(arffData.getBytes(StandardCharsets.UTF_8));
            DataSource source = new DataSource(stream);
            Instances data = source.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);
            this.classifier = new LibSVM();
            this.classifier.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_POLYNOMIAL, LibSVM.TAGS_KERNELTYPE));
            this.classifier.setSVMType(new SelectedTag(LibSVM.SVMTYPE_C_SVC, LibSVM.TAGS_SVMTYPE));
            
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(this.classifier, data, 10, new Random(1));
            
            this.classifier.buildClassifier(data);
            return eval;
        }catch(Exception ex){
            Logger.getLogger(PredictionService.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        return null;
    }
    
    public Room predict(PredictionRequest request){
        try{

            String arffData = this.generateTestData(request);
            StringReader reader = new StringReader(arffData);
            Instances unlabeled = new Instances(reader);
            System.out.println("Test data size: " + unlabeled.size());
            unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
            Instances labeled = new Instances(unlabeled);
            Double clsLabel = this.classifier.classifyInstance(unlabeled.get(0));
            labeled.instance(0).setClassValue(clsLabel);
            String roomIdString = unlabeled.classAttribute().value(clsLabel.intValue());
            
            Long roomId = Long.parseLong(roomIdString);
            Room predictedRoom = RoomService.getInstance().getById(roomId);
            System.out.println(clsLabel + " -> " + roomIdString + " -> " + predictedRoom.getName());
            return predictedRoom;
            
            
        }catch(Exception ex){
           Logger.getLogger(PredictionService.class.getName()).log(Level.SEVERE, null, ex); 
        }
        return null;
    }
}
