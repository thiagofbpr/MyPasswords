package mp.utils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class ToolkitUtil {

    private ToolkitUtil() {

    }

    public static void copyToClipBoard(String text) {
        Toolkit.getDefaultToolkit()
            .getSystemClipboard()
            .setContents(
                    new StringSelection(text),
                    null
        );
    }


}
