/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cls;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author bagong
 */
public class ClassDB {
    private static Connection koneksi;
    public static Connection getkoneksi() {
        if (koneksi==null) {
            try {
                String url=new String();
                String user=new String();
                String password=new String();
                url="jdbc:mysql://localhost:3306/snake";
                user="root";
                password="root";
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                koneksi=DriverManager.getConnection(url,user,password);
               // JOptionPane.showMessageDialog(null,"Koneksi Berhasil");
            }catch (SQLException t) {
                JOptionPane.showMessageDialog(null,"Error membuat koneksi");
            }
        }
     return koneksi;
    }
}
