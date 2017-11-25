package com.junction.exep.dunderi;


import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileManager {

    private Context con;

    public FileManager(Context con) {
        this.con = con;
    }

    public ArrayList<String> readData(){
        ArrayList<String> data = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(con.getAssets().open("nimikkeet.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                data.add(mLine);
            }
        } catch (Exception e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    //log the exception
                }
            }
        }
        return data;
    }
}
