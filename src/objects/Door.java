package objects;

import static utilities.Constants.ObjectConstant.DOOR;
import static utilities.LoadSave.*;

public class Door extends GameObject{
    public Door() {
        objectName = DOOR;
        image = GetSpriteAtlas(DOOR_IMAGE);
    }
}
