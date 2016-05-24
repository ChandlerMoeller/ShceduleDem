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

   public String month;
   public String day;
   public String time;
   public String people;
   public String count;

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

}
