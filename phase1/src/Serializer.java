import java.io.*;
public class Serializer {
    /**
     * Serialize the Object input. Generate a .ser file.
     *
     * @param input the input object to be serialized
     * @param outputPath the path of the serialized output file
     * @throws IOException Exception caused by file reading error
     */
    void saveToFile(Object input, String outputPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputPath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(input);
        oos.close();
    }

    /**
     * De-serialize the .ser file. Return an Object which is the equivalent .txt file.
     *
     * @param inputPath the path of the input File to be de-serialized to
     * @return the Object that is de-serialized from the .ser file
     * @throws IOException Exception caused by reading file error
     * @throws ClassNotFoundException Exception caused by that the serialized file is not a File
     */
    Object readFromFile(String inputPath) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(inputPath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object o = ois.readObject();
        ois.close();
        return o;
    }
}
