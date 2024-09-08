package utility.dataHandler;

import utility.popUp.AlertPopUp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {
    /**
     * Removes empty lines from a file.
     * Reads the file line by line, skips empty lines, and writes the non-empty lines to a temporary file.
     * Renames the temporary file to replace the original file.
     * @param filePath The path of the file to remove empty lines from.
     */
    public static void removeEmptyLines(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".tmp"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            AlertPopUp.generalError(e);
        }

        File originalFile = new File(filePath);
        File tempFile = new File(filePath + ".tmp");
        if (tempFile.renameTo(originalFile)) {
            tempFile.delete();
        }
    }

    /**
     * Retrieves the application path.
     * Returns the absolute path of the current working directory.
     * @return The application path.
     */
    public static String getApplicationPath() {
        Path currentPath = Paths.get("");
        String absolutePath = currentPath.toAbsolutePath().toString();
        return absolutePath;
    }

    /**
     * Initializes a service by creating a data folder and a file if they do not exist.
     * Checks if the data folder exists, and if not, creates it.
     * Checks if the file exists, and if not, creates it.
     * Returns the path of the created file.
     * @param fileName The name of the file to initialize.
     * @return The path of the created file.
     */
    public static String initializeService(String fileName){
        String folderName = "data";
        String filePath = "";
        String applicationPath = FileHandler.getApplicationPath();
        String folderToPath = Paths.get(applicationPath, folderName).toString();
        Path pathFolder = Paths.get(folderToPath);

        // Check if the folder does not exist
        if (!Files.exists(pathFolder)) {
            try {
                // Create the folder
                Files.createDirectories(pathFolder);
                System.out.println("Folder created successfully.");
            } catch (Exception e) {
                System.out.println("Error creating folder: " + e.getMessage());
            }
        }

        filePath = Paths.get(folderToPath, fileName).toString();
        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println( fileName + " created");
            } catch (IOException e) {
                AlertPopUp.generalError(e);
            }
        }
        return filePath;
    }

    /**
     * Generates an ID for a new data entry based on the existing data in a file.
     * Reads the file and retrieves the last ID from the data entries.
     * Increments the last ID by 1 to generate a new ID.
     * @param filePath The path of the file containing the data entries.
     * @param dataLength The expected length of each data entry.
     * @return The generated ID.
     */
    public static Integer generateId(String filePath, Integer dataLength){
        String lastID = null;
        int newID = 0;

        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == dataLength) {
                    lastID = data[0];
                }
            }
        }catch (IOException ioException){
            AlertPopUp.generalError(ioException);
        }
        newID = (lastID != null) ? Integer.parseInt(lastID) + 1 : 1;
        return newID;
    }

}
