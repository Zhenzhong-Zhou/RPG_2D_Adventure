package objects;

import static utilities.Constants.ObjectConstant.CHEST;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.CHEST_IMAGE;

public class Chest extends GameObject{
    public Chest() {
        objectName = CHEST;
        image = GetSpriteAtlas(CHEST_IMAGE);
    }
}
