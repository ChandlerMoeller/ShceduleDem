package com.cs115.shceduledem;

import java.util.ArrayList;

public class ScheduleElement {
   ScheduleElement() {
       month = "";
       day = "";
       time = "";
       people = "";
       count = "0";
   };

    ScheduleElement(String name, ArrayList<String> peepz) {
        month = "";
        day = "";
        time = "";
        people = "";
        count = "0";
        int count = 0;
        for (String s: name.split("::")
             ) {
            switch(count){
                case 0:
                    month = s;
                    break;
                case 1:
                    day = s;
                    break;
                case 2:
                    time = s;
                    break;
                default:
                    break;

            }
            ++count;
        }
        count = 0;
        for (String person: peepz
             ) {
            people += person + ":OK,";
            count++;
        }
        this.count = ""+count;
    };
   public String month;
   public String day;
   public String time;
   public String people;
   public String count;

    public String toString(){
        return "Month: " + month + ", Day: " + day +
                ", Time: " + time + ", People: " + people +
                ", Count: " + count;
    }

    /**
     * Precondition: people is a string that follows regex A(,A)*
     *               where A follows [name]:[[OK]+[NO]]
     * @return
     */
    public ArrayList<String> getArrayListOfPeople(){
        ArrayList<String> toReturn = new ArrayList<String>();
        for (String response: people.split(",")
             ) {
            if(response.length()>0){
                toReturn.add(response.substring(0,response.indexOf(':')));
            }
        }
        return toReturn;
    }

    public String getName(){
        return month+"::"+day+"::"+time;
    }

    public Boolean canTable(String name){
        for (String response: people.split(",")
             ) {
            if(response.length()==0)continue;

            int colonPlace = response.indexOf(':');

            if(name.compareTo(response.substring(0,colonPlace)) == 0){
                return response.substring(colonPlace+1, response.length()).compareTo("OK")==0;
            }
        }

        return false;
    }

    public ArrayList<String> getCanTableList(){
        ArrayList<String> toReturn = new ArrayList<String>();

        for (String response: people.split(",")
                ) {
            if(response.length()==0)continue;

            int colonPlace = response.indexOf(':');

            if(response.substring(colonPlace+1, response.length()).compareTo("OK")==0){
                toReturn.add(response.substring(0,colonPlace));
            }
        }

        return toReturn;
    }

}
