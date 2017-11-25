package com.junction.exep.dunderi;

import org.json.JSONObject;

/**
 * Created by exep on 11/25/17.
 */

public class Duuni {

    private String json, title, company, desc, image, location;

    public Duuni(String json){
        this.json = json;
        try {
            JSONObject js = new JSONObject(json);
            this.title = js.getString("heading");
            this.company = js.getString("company_name");
            this.desc = js.getString("descr");
            this.image = js.getString("export_image_url");
            this.location = js.getString("location");
        } catch(Exception e){

        }
    }

    public String getJson(){
        return json;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }
}
