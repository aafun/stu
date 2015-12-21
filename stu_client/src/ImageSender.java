import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ImageSender {
    int port = 8821;
    //String ip = "115.159.110.34";
    String ip = "localhost";

    void send(String filePath) {
        Socket s = null;
        ClientSocket cs = null;
        try {
            cs = new ClientSocket(ip, port);
            cs.CreateConnection();
            s = cs.getSocket();
            File fi = new File(filePath);
            System.out.println("File Length:" + (int) fi.length());
            DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            DataOutputStream ps = new DataOutputStream(s.getOutputStream());
            ps.writeUTF(fi.getName());
            ps.flush();
            ps.writeLong((long) fi.length());
            ps.flush();

            int bufferSize = 8192;
            byte[] buf = new byte[bufferSize];

            while (true) {
                int read = 0;
                if (fis != null) {
                    read = fis.read(buf);
                }
                if (read == -1) {
                    break;
                }
                ps.write(buf, 0, read);
            }
            ps.flush();
            fis.close();
            System.out.println("File transfer done!");
            InputStreamReader streamReader = new InputStreamReader(s.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            String result =reader.readLine();
            System.out.println("Result: "+result);
            reader.close();
            cs.shutDownConnection();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String arg[]) {
        new ImageSender().send("C:\\Program Files\\Git\\workspace\\stu\\pic\\18.png");
    }
}