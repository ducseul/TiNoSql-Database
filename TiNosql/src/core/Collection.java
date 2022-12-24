/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * @author Ducnm62
 */
public class Collection implements Serializable{
    private Hashtable<String, String> collection;

    public Collection() {
        collection = new Hashtable<>();
    }

    public Hashtable<String, String> getCollectionData() {
        return collection;
    }

    public void setCollection(Hashtable<String, String> dataSrc) {
        this.collection = dataSrc;
    }
    
    
    
}
