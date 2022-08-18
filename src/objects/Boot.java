package objects;

import static utilities.Constants.ObjectConstant.BOOT;
import static utilities.Constants.ObjectConstant.KEY;
import static utilities.LoadSave.*;

public class Boot extends GameObject{
    public Boot() {
        objectName = BOOT;
        image = GetSpriteAtlas(BOOT_IMAGE);
    }
}
