package adrenaline.client.view;

import adrenaline.client.view.ClientCli;
import org.junit.jupiter.api.Test;

public class ClientCliTest {

    @Test
    void InitialTest() {

        ClientCli clientCli = new ClientCli();
        clientCli.InitialClientCli();
    }

    @Test
    void ColorPrintTest() {

        for (int i=0; i<=300;i++ )
            System.out.println("\u001B[" + i + "m This is for Test \u001B[0m i = " + i);

    }
}
