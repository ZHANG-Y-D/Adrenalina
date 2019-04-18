package server.model;



import org.junit.jupiter.api.Test;

class PlayerTest {


    @Test
    void PrintPlayInfo(){

        Player player = new Player("ZHANG",Color.BLUE);
        System.out.println(player.toString());


    }

}
