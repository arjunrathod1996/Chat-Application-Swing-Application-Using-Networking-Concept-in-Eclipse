import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    /* Constructor */
    public Server() {

        try {
            server = new ServerSocket(6666);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting.....");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void startReading() {
        /* Thread --> read */

        Runnable r1 = () -> {
            System.out.println("Reader Started");

            try{
            while (true) {
                
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }

                    System.out.println("Client :" + msg);

            }

        }catch(Exception e){
            // e.printStackTrace();
            System.out.println("Connection closed...");
        }

        };

        new Thread(r1).start();

    }

    public void startWriting() {
        /* Thread --> wtite */

        Runnable r2 = () -> {
            System.out.println("Writer Started...");

            try{
            while(true && !socket.isClosed()){
                

                    

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();

                    out.println(content);

                    out.flush();

                    if(content.equals("exit")){

                        socket.close();
                        break;
                    }


                
            }

        }catch(Exception e){
            // e.printStackTrace();
            System.out.println("Connection closed...");
        }

        // System.out.println("Connection closed...");
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is Server....\nGoing to Start");

        new Server();
    }
}