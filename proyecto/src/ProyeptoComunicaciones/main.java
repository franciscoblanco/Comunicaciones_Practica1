package ProyeptoComunicaciones;

import java.awt.Color;
import java.awt.Font;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
/**
 *
 * @author Francisco Blanco
 */
public class main {
    public static JFrame v=new JFrame();
    public static JButton e= new JButton("EMITIR");
    public static JButton r= new JButton("RECIBIR");
    public static JTextField ip =new JTextField();
    public static boolean sw =false;
    public static String mensaje="";
    public static Socket socket ;
    public static DataOutputStream out = null;
    
    public static Timer tr =new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServerSocket socket;
                try {
                    socket = new ServerSocket(5001);
                    Socket socket_cli = socket.accept();
                    DataInputStream in = new DataInputStream(socket_cli.getInputStream());
                    
                    while (!mensaje.equals("fin")) {
                        mensaje = in.readUTF();
                        if (in.readUTF() != null ) {
                                String[] aux = mensaje.split("-");
                                int x = Integer.parseInt(aux[0]);
                                int y = Integer.parseInt(aux[1]);
                            if(!(x < 0 || y < 0 || x >v.getWidth() || y > v.getHeight())){   
                                v.getGraphics().fillRect(x, y, 5, 5);
                            }
                           
                        }
                    } 
                } catch (IOException ex) {
                }

            }
        });

    public static void main(String[] args) {
        cargar_ventana();
    }
    public static void cargar_ventana(){
        v.setVisible(true);
        v.setBackground(Color.WHITE);
        v.setLayout(null);
        v.setExtendedState(MAXIMIZED_BOTH);
        e.setSize(v.getWidth(), (int) (v.getHeight()*0.4));
        r.setSize(v.getWidth(), (int) (v.getHeight()*0.4));
        ip.setSize(v.getWidth(), (int) (v.getHeight()*0.2));
        ip.setFont(new Font("BOLD", 1, (int) (v.getHeight()*0.2)));
        e.setLocation(0, 0);
        ip.setLocation(0,(int)(v.getHeight()*0.4));
        r.setLocation(0,(int)(v.getHeight()*0.6));
        v.add(ip);
        v.add(e);
        v.add(r);
//        v.getGraphics().setColor(Color.BLACK);
        v.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if(!tr.isRunning() && socket!=null){
                    try {
                        out.writeUTF("fin");
                    } catch (IOException ex) {
                }
                }else{
                    tr.stop();
                }
                System.exit(0);
            }
            @Override
            public void windowClosing(WindowEvent e) {
                if(!tr.isRunning() && socket!=null){
                    try {
                        out.writeUTF("fin");
                    } catch (IOException ex) {
                }
                }else{
                    tr.stop();
                }
                System.exit(0);
            }
            @Override
            public void windowActivated(WindowEvent e) {
            
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            
            }
        });
        cargar_funciones_botones();
    }
    public static void quitar_componentes(){
        ip.setVisible(false);
        e.setVisible(false);
        r.setVisible(false);
        v.remove(ip);
        v.remove(e);
        v.remove(r);
    }
    public static void cargar_funciones_botones(){
        e.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!ip.getText().equals("")){
            try {
                socket = new Socket(ip.getText(), 5001);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,"IP no valida");
                return;
            }
                    quitar_componentes();
                    cargar_funciones_ventana();
            try {
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
            }
                }else{
                    JOptionPane.showMessageDialog(null,"Falta IP");
                }
            }

        });
        r.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                quitar_componentes();
                tr.start();
            }

        });
    }
    public static void cargar_funciones_ventana(){ 
        v.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {               
            v.getGraphics().fillRect(e.getX(), e.getY(), 5, 5);
                mensaje = e.getX() + "-" + e.getY();
                try {
                    out.writeUTF(mensaje);
                } catch (IOException ex) {
                }
            }             

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }
    
}
