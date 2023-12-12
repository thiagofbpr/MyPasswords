package mp.utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    private static final int IMAGE_SIZE = 24;
    private static final String GOOGLE_SERVICE_URL = "https://www.google.com/s2/favicons?sz=" + IMAGE_SIZE + "&domain_url=";
    public static final Image DEFAULT_APP_LOGO = new Image(ImageUtil.class.getResource("/images/icon-app-default.png").toString());

    private ImageUtil() {

    }

    public static Image searchAppLogo(String website) {
        if (canStartSearchingWebsiteLogo(website)) {
            String logoUrl = GOOGLE_SERVICE_URL + website;
            return new Image(logoUrl);
        }
        return null;
    }

    private static boolean canStartSearchingWebsiteLogo(String text) {
        final String http = "http://";
        final String https = "https://";
        final String www = "www";
        final String dot = ".";

        return ((text.contains(http) || text.contains(https) || text.contains(www)) && text.indexOf(dot) != text.lastIndexOf(dot))
                || (!text.contains(http) && !text.contains(https) && !text.contains(www) && text.contains(dot));
    }

    public static byte[] imageToByteArray(Image image, String formatName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), formatName, baos);
        return baos.toByteArray();
    }

    public static byte[] streamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = is.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        is.close();
        return baos.toByteArray();
    }

    public static FontAwesomeIcon createIcon(FontAwesomeIcons fontIcon, boolean clickable) {
        FontAwesomeIcon icon = new FontAwesomeIcon();
        icon.setIcon(fontIcon);
        icon.fillProperty().set(Color.valueOf("#425348"));
        icon.setSize("1.2em");
        if (clickable) {
            icon.getStyleClass().add("clickable-icon");
        }
        return icon;
    }


}
