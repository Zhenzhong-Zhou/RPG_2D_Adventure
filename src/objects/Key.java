package objects;

import static utilities.Constants.ObjectConstant.KEY;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.KEY_IMAGE;

public class Key extends GameObject {
    public Key() {
        objectName = KEY;
        image = GetSpriteAtlas(KEY_IMAGE);
    }
}
