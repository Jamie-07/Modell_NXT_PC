
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import lejos.pc.comm.NXTConnector;

/**
 * This is a PC sample. It connects to the NXT, and then
 * sends an integer and waits for a reply, 100 times.
 * 
 * Compile this program with javac (not nxjc), and run it 
 * with java.
 * 
 * You need pccomm.jar and bluecove.jar on the CLASSPATH. 
 * On Linux, you will also need bluecove-gpl.jar on the CLASSPATH.
 * 
 * Run the program by:
 * 
 *   java BTSend 
 * 
 * Your NXT should be running a sample such as BTReceive or
 * SignalTest. Run the NXT program first until it is
 * waiting for a connection, and then run the PC program. 
 * 
 * @author Lawrie Griffiths
 *
 */
public class BTSendPC {	
	public static void main(String[] args) {
		NXTConnector conn = new NXTConnector();

		// Connect to any NXT over Bluetooth
                boolean connected = false;
                int counterConncetion = 0;
                
		while(!connected && counterConncetion <= 20) {
                        connected = conn.connectTo("btspp://");
			System.err.println("(" + counterConncetion + ") Failed to connect to any NXT");
                        
                        if(counterConncetion==20 && !connected) {
                            System.exit(1);
                        }
		}
                
                System.out.println("Connected");
                
                VRManager.create();
		
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		DataInputStream dis = new DataInputStream(conn.getInputStream());
                
                System.out.println("Streams opened.");
                
                int counter = 0;
				
		while(true) {
                    
                        System.out.println(counter++);
                    
			try {
                                String dataString = dis.readUTF();
                                String[] dataArray = dataString.split(";");
                                ArrayList<String> data = new ArrayList(Arrays.asList(dataArray));
                                String[] names = {"speedA", "tcA", "isMovingA", "speedB", "tcB", "isMovingB", "distance", "color", "counterColored", "counterGrey"};
                                HashMap<String, String> dataMap = new HashMap();
                                
                                data.forEach(date -> {
                                    System.out.println("Put date to map -> " + date);
                                    dataMap.put(names[data.indexOf(date)], date);
                                    
                                });
                                
                                System.out.println(dataString);

                                DatabaseConnection dbc = new DatabaseConnection();
                                dbc.storeData(dataMap);
                                
                                
				//System.out.println("Received " + IOUtils.toString(dis, "UTF-8"));
			} catch (IOException ioe) {
				System.out.println("IO Exception reading bytes:");
				System.out.println(ioe.getMessage());
				break;
			}
		}
		
		try {
			dis.close();
			dos.close();
			conn.close();
		} catch (IOException ioe) {
			System.out.println("IOException closing connection:");
			System.out.println(ioe.getMessage());
		}
                finally {
                    
                    String[] names = {"speedA", "tcA", "isMovingA", "speedB", "tcB", "isMovingB", "distance", "color", "counterColored", "counterGrey"};
                    ArrayList<String> data = new ArrayList(Arrays.asList(names));
                    HashMap<String, String> dataMap = new HashMap();

                    data.forEach(name -> {
                        dataMap.put(name, "-");

                    });

                    DatabaseConnection dbc = new DatabaseConnection();
                    dbc.storeData(dataMap);
                    
                    
                    
                }
	}
}
