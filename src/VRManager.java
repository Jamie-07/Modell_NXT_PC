
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import lejos.nxt.FileSystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jan-Peter Schmidt
 */
class VRManager {
    
    public static void create() {
        
        String[] representations = {"machine", "machine/engineA", "machine/engineB", "machine/ultrasonic", "machine/color-sensor", "machine/conveyor"};
        String[] fileName = {"Maschine", "MotorA", "MotorB", "Ultrasonic", "ColorSensor", "Foerderband"};
        final String PATH = "C:\\Users\\Jan-Peter Schmidt\\Desktop\\Masterarbeit\\config\\";
        
        for(int i=0; i<representations.length; i++) {
            
            upload(representations[i], new File(PATH + fileName[i] + ".ttl"), "POST");
            //upload(representations[i], new File(PATH + "standard.rq"), "PUT");
            
        }
        
    }
    
    private static void upload(String name, File file, String method) {
        
        try {

            System.out.println("Start upload...");
            String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
            String CRLF = "\r\n"; // Line separator required by multipart/form-data.      

            //Taken from: https://stackoverflow.com/a/2469587
            URL url = new URL("http://localhost:9999/representations/" + name); 
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream out = con.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"), true);
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").append(CRLF);
            writer.append("Content-Type: application/octet-stream").append(CRLF);
            writer.append(CRLF).flush();
            if(file.exists()) {
                Files.copy(file.toPath(), out);
            } /*else {
                writer.append("dummy");
            }*/
            out.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.                

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
            
            System.out.println("ResponseCode for " + name + ": " + con.getResponseCode());


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }        
    
    
}
