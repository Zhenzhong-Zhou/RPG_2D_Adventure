package objects;

import static utilities.Constants.ObjectConstant.DOOR;
import static utilities.LoadSave.DOOR_IMAGE;
import static utilities.LoadSave.GetSpriteAtlas;

public class Door extends GameObject {
    public Door() {
        objectName = DOOR;
        image = GetSpriteAtlas(DOOR_IMAGE);
    }
}
