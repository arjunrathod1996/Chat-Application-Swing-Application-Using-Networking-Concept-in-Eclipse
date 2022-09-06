import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame{

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    /*------------------- start Declare Components ------------------------ */

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Cursive",Font.PLAIN,15);

     /*------------------- start Declare Components ------------------------ */




    /* ------------ Start Client Constructor --------------------- */
    public Client(){


        try {
            System.out.println("Sending request to server...");
            
            socket = new Socket("127.0.0.1",6666);
            System.out.println("Connected...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            /* ---------------------- create GUI -------------------------- */
            createGUI();

            /* -------------------------- Handle Events ------------------------------------ */
            handleEvents();

            startReading();
            // startWriting();

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ----------------- Handlde Events ---------------- */

    private void handleEvents() {
            messageInput.addKeyListener(new KeyListener(){

                @Override
                public void keyTyped(KeyEvent e) {
                   
                    
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    
                    
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    // System.out.println("key released :" + e.getKeyCode());
                    if(e.getKeyCode()==10){
                        // System.out.println("You Have Presses Enter Button");

                        String contentToSend = messageInput.getText();
                        messageArea.append("Me :" + contentToSend + "\n" );
                        out.println(contentToSend);
                        out.flush();
                        messageInput.setText("");
                        messageInput.requestFocus();
                    }
                    
                }

            });      
    }

    private void createGUI(){

        /*------------------------- GUI code ------------------------------ */

        this.setTitle("Client Messanger[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /*---------------- Coding for Component -----------------------*/

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        
        messageArea.setEditable(false);
        

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));


        /*----------- Frame Layout ----------------------------------- */

        this.setLayout(new BorderLayout());

        /*------------------ Adding the components ------------------ */
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add( messageInput,BorderLayout.SOUTH);


        this.setVisible(true);

    }

    /* ------------ Start Client Constructor --------------------- */


    /* ------------ Start Start Reading Method --------------------- */
    public void startReading() {
        /* Thread --> read */

        Runnable r1 = () -> {
            System.out.println("Reader Started");
            try {
            while (true) {
                
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated The Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    // System.out.println("Server :" + msg);
                    messageArea.append("Server :" + msg + "\n");
                

            }
        } catch (Exception e) {
            // e.printStackTrace();

            System.out.println("Connection closed...");
        }

        };

        new Thread(r1).start();

    }


    /* ------------ End Start Reading Method --------------------- */


    /* ------------ Start Start Writing Method --------------------- */

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

            System.out.println("Connection closed...");
        }catch(Exception e){
            e.printStackTrace();
        }
        };

        new Thread(r2).start();
    }

    /* ------------ End Start Reading Method --------------------- */

    public static void main(String[] args) {
        System.out.println("This is Client...");

        new Client();
    }
}
