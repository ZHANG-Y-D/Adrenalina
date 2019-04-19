

/*
 *  This is class for single AmmoCard.
 *
 *  Author Zhang YueDong
 *
 *
 *
 *  Attention from Zhang Yuedong:
 *
 *     Because the Gson or Other Json API,read object is the mode
 *     "no-argument (default) construtor to instantiate an object"
 *     SO now we just memory AmmoCard Like a char array, when use time
 *     we can use the getter method  getAmmoContent to get a int array
 *
 *
 *
 */



package server.model;


public class AmmoCard {

    private String ammoContent;   //Seq: r b y p   r=red b=blue y=yellow p=powerup
    private int numAmmoCard;      //This num for read the graphic of AmmoCard


    public AmmoCard(String ammoContent, int numAmmoCard) {
        this.ammoContent = ammoContent;
        this.numAmmoCard = numAmmoCard;
    }


    //This method use for Input Ammo Card graphic
    public int getNumAmmoCard() {
        return numAmmoCard;
    }



    //This method transfer String(Es:rbb) to int array(Es: {1,2,0,0})
    public int[] getAmmoContent() {   //Seq: r b y p

        char[] a;
        int[] b;


        a=this.ammoContent.toCharArray();

        b = new int[]{0,0,0,0};


        for (int i=0;i<=2;i++) {
            if (a[i] == 'r')
                b[0]++;
            else if (a[i] == 'b')
                b[1]++;
            else if (a[i] == 'y')
                b[2]++;
            else if (a[i] == 'p')
                b[3]++;
            else
              System.out.print("Ammo.json File Error!!! Check it!!!");  // Here maybe have to add a Exception
        }

        return b;
    }


    //Just for test, Put here a little times
    @Override
    public String toString() {
        return "AmmoCard{" +
                "ammoContent='" + ammoContent + '\'' +
                ", numAmmoCard=" + numAmmoCard +
                '}';
    }

}




