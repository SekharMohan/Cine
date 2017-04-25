//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cine.service.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Params {
    private Map<String, Object> params = new HashMap();
    private String contentType = "plain";

    public Params() {
    }

    public void setContentType(String type) {
        this.contentType = type;
    }

    public void addParam(String name, Object value) {
        this.params.put(name, value);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object o) {
        return super.equals(o);
    }

    public String getParamList() {
        Entry entry;
        Iterator var3;
        if(this.contentType.equals("plain")) {
            StringBuilder json1 = new StringBuilder();
            if(this.params.size() > 0) {
//                json1.append("?");
            }

            var3 = this.params.entrySet().iterator();

            while(var3.hasNext()) {
                entry = (Entry)var3.next();
                json1.append((String)entry.getKey());
                json1.append("=");
                json1.append(entry.getValue());
                json1.append("&");
            }

            return json1.toString();
        } else {
            String json = null;

            for(var3 = this.params.entrySet().iterator(); var3.hasNext(); json = (String)entry.getValue()) {
                entry = (Entry)var3.next();
            }

            return json.toString();
        }
    }

    public Map<String, Object> getParams() {
        return this.params;
    }
}
