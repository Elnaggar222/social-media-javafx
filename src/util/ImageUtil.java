package util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class ImageUtil {

    public static String copyImageToUploads(File sourceFile) throws IOException {

        File uploadsDir = new File("uploads");
        if (!uploadsDir.exists()) uploadsDir.mkdir();

        File destFile = new File("uploads/" +
                System.currentTimeMillis() + "_" + sourceFile.getName());

        Files.copy(sourceFile.toPath(),
                destFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        return destFile.getPath();
    }

    public static void deleteFile(String path) {
        if (path == null) return;

        File file = new File(path);
        if (file.exists()) file.delete();
    }
 // Create ImageView with standard sizing
    public static ImageView createImageView(String imagePath,
                                            double width,
                                            double height) {

    	ImageView imageView = new ImageView();
    	if (imagePath == null) return null;

        File file = new File(imagePath);
        if (!file.exists()) return imageView;
        imageView.setImage(new Image(file.toURI().toString()));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        return imageView;
    }
    // Set preview image
    public static void setPreviewImage(ImageView imageView,
                                       String imagePath,
                                       double width,
                                       double height) {

        ImageView created =
                createImageView(imagePath, width, height);

        if (created != null) {
            imageView.setImage(created.getImage());
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            imageView.setVisible(true);
        }
    }
    // Clear preview
    public static void clearPreview(ImageView imageView) {
        imageView.setImage(null);
        imageView.setVisible(false);
    }
}