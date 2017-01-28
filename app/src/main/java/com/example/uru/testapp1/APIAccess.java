package com.example.uru.testapp1;

import android.os.AsyncTask;
import android.os.Process;
import android.support.annotation.RequiresPermission;
import android.util.JsonReader;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Created by Uru on 10/8/2016.
 */

//      "https://api.guildwars2.com/v2/achievements/daily";
public class APIAccess implements Runnable {

    private String BASE_URL = "https://api.guildwars2.com/v2/achievements";
    private HashMap<Integer, GWAchievement> result;

    public HashMap getResult(){
        return result;
    }

    @Override
    public void run()
    {
//        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
        try{
            URL url = new URL(BASE_URL + "/daily");
            JSONObject jObj = readFromConnection(url);
            result = addInfo(jsonToMap(jObj));
        }
        catch(Exception e)
        {
            result = new HashMap<>();
        }

    }

    private HashMap addInfo(HashMap<Integer, GWAchievement> hm){
        try {
            int size = hm.size();
            for (int i = 0; i < size; ++i) {
                GWAchievement gwa = hm.get(i);
                URL url = new URL(BASE_URL + "/" + gwa.getAchievementID());
                JSONObject jObj = readFromConnection(url);

                gwa.setName(jObj.getString("name"));
                gwa.setDescription(jObj.getString("description"));
                gwa.setObjective(jObj.getString("requirement"));

                URL url2 = new URL("https://api.guildwars2.com/v2/items/" + jObj.getJSONArray("rewards").getJSONObject(0).getString("id"));
                JSONObject jObj2 = readFromConnection(url2);

                gwa.setReward(jObj2.getString("name"));
//                hm.remove(i);
                hm.put(i, gwa);
            }
        }
        catch(Exception e)
        {
           GWAchievement g = new GWAchievement();
            g.setName(e.getMessage());
            hm.put(0, g);
        }

        return hm;
    }

    private JSONObject readFromConnection(URL url)
    {
        try {
            URLConnection uC = url.openConnection();
            try {
                try
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) uC.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    int cp;
                    while ((cp = br.read()) != -1) {
                        sb.append((char) cp);
                    }
                    String jsonText = sb.toString();
                    try {
                        br.close();
                        uC.setConnectTimeout(0);
                        return new JSONObject(jsonText);
                    }
                    catch(org.json.JSONException e) {
                        throw (new RuntimeException(e.toString()));
                    }
//                    finally{
//                        br.close();
//                        uC.setConnectTimeout(0);
//                    }
                }
                catch(Error e) {
                    throw (new RuntimeException(e.toString()));
                }
            }
            catch (Error e) {
                throw (new RuntimeException(e.toString()));
            }
        } catch (java.io.IOException i) {
            throw (new RuntimeException(i.toString()));
        }
    }

    private HashMap jsonToMap(JSONObject jObj)
    {
        HashMap achMap = new HashMap<Integer, GWAchievement>();
        Iterator<String> keyIterator = jObj.keys();
        int i = 0;
        while (keyIterator.hasNext())
        {
            String currentType = keyIterator.next();
            try{JSONArray jArray = jObj.getJSONArray(currentType);
                int counter = 0;
                while (counter < jArray.length())
                {
                    //keyIterator2.next() is each achievement object...probably
                    JSONObject achievement = jArray.getJSONObject(counter);
                    achMap.put(i, new GWAchievement(currentType, achievement.getString("id"),
                            achievement.getJSONObject("level").getInt("min"),
                            achievement.getJSONObject("level").getInt("max")));
                    ++i;
                    ++counter;
                }
            } catch(org.json.JSONException e){throw(new RuntimeException(e.toString()));}
        }
        return achMap;
    }
}
