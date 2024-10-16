
package tester;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Raul.Cuevas
 */
public class recibir extends Thread {

    private final javax.swing.JTextArea x_as;
    private final javax.swing.JTextArea x_he;
    private final Socket x_soc;
    private boolean apagar=false;
            
    public recibir(Socket _soc, javax.swing.JTextArea _as, javax.swing.JTextArea _he) {
        x_he=_he;
        x_as=_as;
        x_soc=_soc;
        _he.setText("");
        _as.setText("");
    }
    
    public void Apagar() {
        this.apagar=true;
    }
    
    @Override 
    public void run() {  
        try (DataInputStream entrada = new DataInputStream(x_soc.getInputStream())) {
            do {
                int y_byte=entrada.read();
                if (y_byte>=0) {
                    if (y_byte<16) this.x_he.append("0");
//System.out.println("byte: " + y_byte);
                    this.x_he.append(Integer.toHexString(y_byte).toUpperCase());
        switch (y_byte) {
                        case 10 -> this.x_as.append("\n");
                        case 13 -> this.x_as.append("\r");
                        case 32 -> this.x_as.append(" ");
                        default -> {
                            if (y_byte>32) this.x_as.append(String.valueOf((char)y_byte));
                            else this.x_as.append("#");
                        }
                    } 
                }    
            } while (!this.apagar);
        } catch(IOException e) {
            
        }
    }
    
    
}
