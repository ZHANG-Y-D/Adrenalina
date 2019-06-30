package adrenaline.server.model;

import adrenaline.Color;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AvatarTest {

    @Test
    void avatarsBuildTest() {
        Gson gson = new Gson();
        Avatar[] avatarsGson= gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/Jsonsrc/Avatar.json")),Avatar[].class);
        ArrayList<Avatar> avatars = new ArrayList<>(Arrays.asList(avatarsGson));

        //Test avatars total
        assertEquals(5,avatars.size());

        //Test avatars contains all colors
        ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.YELLOW,Color.BLUE,Color.PURPLE,Color.GRAY,Color.GREEN));
        for(Avatar avatar : avatars) colors.removeIf(x -> colors.contains(avatar.getColor()));
        assertTrue(colors.isEmpty());

        //Test avatars contains all names
        ArrayList<String> names = new ArrayList<>(Arrays.asList(":D-STRUCT-OR","BANSHEE","DOZER","VIOLET","SPROG"));
        for(Avatar avatar : avatars) names.removeIf(x -> names.contains(avatar.getName()));
        assertTrue(names.isEmpty());
    }
}
