package server.model;/*
 *  This is class for single AmmoCard
 *
 *  Author Zhang YueDong
 */


public class AmmoCard {

    private int[] ammoContent;   //Seq: r b y p   p=puwerup b=blue r=red y=yellow
    private int numAmmoCard;


    public AmmoCard(String ammo, int num) {

        char[] a;
        int i;

        numAmmoCard = num;

        a=ammo.toCharArray();

        ammoContent = new int[]{0,0,0,0};

        for (i=0;i<=2;i++){
            if (a[i] == 'r')
                ammoContent[0]++;
            else if (a[i] == 'b')
                ammoContent[1]++;
            else if (a[i] == 'y')
                ammoContent[2]++;
            else if (a[i] == 'p')
                ammoContent[3]++;
            else
                ; //exception
        }

    }



}



