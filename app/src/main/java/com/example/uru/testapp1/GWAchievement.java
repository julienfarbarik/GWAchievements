package com.example.uru.testapp1;

/**
 * Created by Uru on 10/11/2016.
 */

public class GWAchievement {
    private String aType, aID, aName, aDesc, aObjective, aReward;
    private int achMinLevel, achMaxLevel;

    public GWAchievement(){
        this.aType = "";
        this.aID = "";
        this.aName = "";
        this.aDesc = "";
        this.aObjective = "";
        this.aReward = "";
        this.achMinLevel = 1;
        this.achMaxLevel = 2;
    }

    public GWAchievement(String aT, String aI, int aMin, int aMax)
    {
        this.aType = aT;
        this.aID = aI;
        this.aName = "";
        this.aDesc = "";
        this.aObjective = "";
        this.aReward = "";
        this.achMinLevel = aMin;
        this.achMaxLevel = aMax;
    }

    public void setName(String s){
        aName = s;
    }
    public void setDescription(String s){
        aDesc = s;
    }
    public void setObjective(String s){
        aObjective = s;
    }
    public void setReward(String s){
        aReward = s;
    }

    public String getName(){
        return aName;
    }
    public String getAchievementID(){
        return aID;
    }
    public String getObjective(){
        return aObjective;
    }


}
