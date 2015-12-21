import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ResultReceiver implements Runnable {
    int port = 8821;
    //String ip = "115.159.110.34";
    String ip = "localhost";

    @Override
    public void run() {
        Socket s = null;
        ClientSocket cs = null;

        try {
            cs = new ClientSocket(ip, port);
            cs.CreateConnection();
            s = cs.getSocket();
            if (s != null) System.out.println("结果收集器已启动！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new ResultReceiver()).start();
    }
}