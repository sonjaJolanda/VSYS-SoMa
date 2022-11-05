package AB_02_EX_03;

import java.io.*;

public class MySerializer {
    private MySerializableClass mySerializableClass;

    MySerializer(MySerializableClass serializableClass) {
        mySerializableClass = serializableClass;
    }

    private String readFilename() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("filename> ");
        return reader.readLine();
    }

    public void write(String text) throws IOException {
        mySerializableClass.set(text);
        //System.out.println("nonSer:" + mySerializableClass.toString());
        String filename = readFilename();
        try {
            // open file for writing; if non-existent, create it
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename));
            output.writeObject(mySerializableClass);
            output.close();
        } catch (IOException e) {
            System.out.println("An error occurred." + e);
        }
    }

    public String read() throws IOException, ClassNotFoundException {
        String filename = readFilename();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename));
            mySerializableClass = (MySerializableClass) new ObjectInputStream(new FileInputStream(filename)).readObject();
            input.close();
        } catch (IOException e) {
            System.out.println("An error occurred." + e);
        }
        return mySerializableClass.toString();
    }

    /*public void createFile(String filename) {
        String pathString = pathToSavedFiles + filename + ".txt";

        try {
            File file = new File(pathString);
            System.out.println(file.createNewFile() ? "File created: " + file.getName() : "File already exists.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Path path = Path.of(pathString);
        try {
            Files.writeString(path, this.mySerializableClass.toString());
        } catch (Exception e) {
            System.out.println("An error occured!");
        }
    }*/

    /*public String readFile(String filename) {
        String pathString = pathToSavedFiles + filename + ".txt";
        Path path = Path.of(pathString);
        try {
            String content = Files.readString(path);
            //System.out.println(content);
            return content;
        } catch (Exception e) {
            System.out.println("An error occured!");
            return "An error occured!";
        }
    }*/
} 
	