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
public class DataRecord_Test {
    
    @JsonProperty
    Long count;

    public DataRecord_Test() {
    }

    public DataRecord_Test(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public String toString() {
        return new com.google.gson.Gson().toJson(this);
    }
}
