package tester;

import java.net.Socket;
import java.net.InetAddress;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author raul
 */
public class enviar {
    
    private Socket cliente=null;
    private DataOutputStream salida=null;
    private final tester.recibir Recibe;

    public enviar(String _ip, String _pto, javax.swing.JTextArea _as, javax.swing.JTextArea _he) throws Exception {
        try {
            InetAddress dir = InetAddress.getByName(_ip);
            int pto=Integer.valueOf(_pto);
            cliente = new Socket(dir, pto);
            _as.setText("");
            _he.setText("");
            this.Recibe=new tester.recibir(cliente, _as, _he);
            this.Recibe.start();
            salida = new DataOutputStream(cliente.getOutputStream());
        } catch (IOException | NumberFormatException e) {
            throw new Exception("Error conexion");
        }
    }
    
    public boolean flushASCII(String _cadena, String _desp) {
        try {
            int y_code=0;
            if (_desp!=null) {
                if (!_desp.isEmpty()) {
                    try {
                        y_code=Integer.parseInt(_desp);
                        if ((y_code<1)||(y_code>4)) y_code=0;
                    } catch (NumberFormatException e1) {
                        y_code=0;
                    }
                }
            }
            byte[] y_cad=_cadena.getBytes();
            if (y_code>0) {
                int y_byte;
                for (int y_x=0;y_x<y_cad.length;y_x++) {
                    y_byte=255&y_cad[y_x];
//System.out.print(":" + y_byte + "-");                    
                    int y_ab=y_byte>>(8-y_code);
                    y_byte=y_byte<<y_code;
                    y_byte=(y_byte|y_ab);
                    y_byte=y_byte&255;
//System.out.print(y_byte);  
                    y_cad[y_x]=(byte)y_byte;
                }
            }
            salida.write(y_cad);
            salida.flush();
        } catch (IOException e) {
            return false;
        }    
        return true;
    }
    
    public int flushHEX(String _cadena) {
        char[] cha=_cadena.toCharArray();
        if ((cha.length%2)!=0) return -1;
        try {
            int l=(cha.length/2);
            byte[] b=new byte[l];
            int i=0;
            int j=0;
            while (i<cha.length) {
                int x=Integer.parseInt(String.valueOf(cha[i]).concat(String.valueOf(cha[++i])),16);
                b[j]=(byte)x;
                ++j;
                ++i;
            }
            salida.write(b);
            return j;
        } catch (IOException | NumberFormatException e) {
            return -2;
        }    
    }
    
    public void apagar () {
        try {
            this.Recibe.Apagar();
            if (cliente!=null) {
                salida.close();
                cliente.close();
            }
            this.Recibe.join();
        } catch (IOException | InterruptedException e) {}    
    }     
}
