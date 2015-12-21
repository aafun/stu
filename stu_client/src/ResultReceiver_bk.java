import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ResultReceiver_bk implements Runnable {
    private int port = 8822;

    @Override
    public void run() {
        Socket s = null;
        ServerSocket ss;

        try {
            ss = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("结果接收服务启动完成！");
        while (true) {
            try {
                s = ss.accept();

                System.out.println("" + s.getInetAddress().getCanonicalHostName() + " - "+ s.getPort() );
                System.out.println("建立socket链接");

                DataInputStream fis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                String result = fis.readUTF();
                System.out.println(result);

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new ResultReceiver_bk()).start();
    }
}