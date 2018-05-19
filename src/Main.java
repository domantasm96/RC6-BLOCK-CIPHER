import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length == 4 ){
            if(args[0].equals("-e")){
                encryption(args[1], args[2], args[3]);

            }
            else if(args[0].equals("-d")){
                decryption(args[1], args[2], args[3]);
            }
        }
        else{
            System.out.println("Arguments not found. \n" +
                    "Arguments list: \n" +

                    "java Main -e textFile keyFile encryptionFile\n Encryption mode with paths to text file and key file. The last argument is encrypted text filename\n" +
                    "java Main -d encryptionFile keyFile outputFile\n Decryption mode with paths to encrypted text file and key file. The last argument is decryption output\n"+
                    "----------------------------------------------------------------------------------------\n"+
                    "Example:\n"+
                    "Encryption: java Main -e text key enc\n"+
                    "Decryption: java Main -d enc key output"
            );


        }
    }

    private static void encryption(String textFile, String keyFile, String outputFile){
        try{
            Path plainText_file = Paths.get(textFile);
            Path key_file = Paths.get(keyFile);

            byte[] text_byte = Files.readAllBytes(plainText_file);
            byte[] key_byte =  Files.readAllBytes(key_file);
            if(key_byte.length > 3){
                byte[] enc = rc6.encrypt(text_byte, key_byte);

                PrintWriter encryptFile = new PrintWriter("Encrypted_files/"+outputFile);
                encryptFile.write(printBytes(enc));
                encryptFile.close();

                System.out.println("*********** Encryption is completed. Encrypted text is saved in "+outputFile + " file ******************");
            }
            else{
                System.out.println("Key symbols length should be >= 4\n");
            }
        }
        catch (Exception e){
            System.out.println("Check if plain text file or key file exists.");
            return;
        }
    }

    private static void decryption(String encryptedFile, String keyFile, String outputFile){
        try{
            Path key_file = Paths.get(keyFile);
            BufferedReader text = new BufferedReader(new FileReader("Encrypted_files/"+encryptedFile));

            byte[] encrypt_byte = stringToBytes(text.readLine());
            byte[] key_byte =  Files.readAllBytes(key_file);
            if(key_byte.length > 3){
                byte[] dec = rc6.decrypt(encrypt_byte, key_byte);

                PrintWriter decryptFile = new PrintWriter("Decrypted_files/"+outputFile);
                decryptFile.write(decimalToHex(dec));
                decryptFile.close();

                System.out.println("-----------------------DECRYPTION TEXT--------------------------\n\n"+new String(dec)+"\n");
                System.out.println("*********** "+encryptedFile+" File decryption is done. Encrypted text is saved in "+outputFile + " file ******************");
            }
           else{
                System.out.println("Key symbols length should be >= 4\n");
            }
        }
        catch(Exception e){
            System.out.println("Check if encrypted file or key file exists.");
            return;
        }
    }



    private static String decimalToHex(byte[] array){
        String hex = "";
        for(int i = 0; i < array.length; i++){
            hex += Integer.toHexString(array[i]);
        }
        return hex;
    }

    private static String printBytes(byte[] array){
        String bits = "";
        for(int i = 0; i < array.length; i++){
            bits += i != array.length - 1 ? array[i] + " " : array[i];
        }
        return bits;
    }

    private static byte[] stringToBytes(String text){
        String[] split = text.split(" ");
        byte[] encryption = new byte[split.length];
        for(int i = 0; i < split.length; i++){
            encryption[i] = (byte)Integer.parseInt(split[i]);
        }
        return encryption;
    }

}

