/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.util.Comparator;

/**
 *
 * @author RadimP
 */
public class Datescomparator implements Comparator<String> {

    @Override
    public int compare(String t, String t1) {
     String[] parts1 =  t.split("\\.");
     String[] parts2 =  t1.split("\\.");
      if(parts1[1].length()<2){parts1[1]="0"+parts1[1];}
      if(parts1[0].length()<2){parts1[0]="0"+parts1[0];}
      if(parts2[1].length()<2){parts2[1]="0"+parts2[1];}
      if(parts2[0].length()<2){parts2[0]="0"+parts2[0];}
     String reversed1=parts1[2]+parts1[1]+parts1[0];
     String reversed2=parts2[2]+parts2[1]+parts2[0];
     return (Integer.parseInt(reversed1)-Integer.parseInt(reversed2));
    }
    
}
