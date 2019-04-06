/*
 *  This is class for single AmmoCard
 *
 *  Author Zhang YueDong
 */


public class AmmoCard {

    private String colorCombinatorial;  //p=puwerup b=blue r=red y=yellow
    private char[] ammoContent;
    private int num;


    public AmmoCard(String colorCombinatorial,int num) {
        colorCombinatorial = colorCombinatorial;
        num = num;
        ammoContent = new char[3];
        ammoContent = colorCombinatorial.toCharArray();

    }



}



