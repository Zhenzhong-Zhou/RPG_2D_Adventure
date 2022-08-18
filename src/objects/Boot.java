package objects;

import static utilities.Constants.ObjectConstant.BOOT;
import static utilities.LoadSave.BOOT_IMAGE;
import static utilities.LoadSave.GetSpriteAtlas;

public class Boot extends GameObject {
    public Boot() {
        objectName = BOOT;
        image = GetSpriteAtlas(BOOT_IMAGE);
    }
}
