/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.rics.uninova.kafka.library.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author joao
 */
public class DataRecord {
    
    @JsonProperty
    private String productID;
    
    @JsonProperty
    private String productDescription;
    
    @JsonProperty
    private String productSpecs;
    
    public DataRecord() {    
    }
    
    public DataRecord(String ID, String description, String specs) {
        this.productID = ID;
        this.productDescription = description;
        this.productSpecs = specs;
    }

    public String getProductID(){
        return this.productID;
    }
    
    public String getProductDescription(){
        return this.productDescription;
    }
    
    public String getProductSpecs(){
        return this.productSpecs;
    }

    public String toString() {
        return new com.google.gson.Gson().toJson(this);
    }
}
