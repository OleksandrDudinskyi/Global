package org.globalsdb.sample.keyvalue;


import com.intersys.globals.impl.ConnectionImpl;

public class Connection extends ConnectionImpl {

    public KeyValueMap getKeyValueMap(String name) {
        return new KeyValueMap(name,this);
    }

}
