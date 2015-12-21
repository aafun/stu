import cn.gdut.StuTool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ImageReceiver {
    private int port = 8821;


    public void start() {
        Socket s = null;
        ServerSocket ss;
        StuTool st = new StuTool();

        try {
            ss = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("服务器启动完成！");
            while (true) {
                try {
                    s = ss.accept();

                    System.out.println("" + s.getInetAddress().getCanonicalHostName() + " - "+ s.getPort() );
                System.out.println("建立socket链接");
                String savePath = "C:\\";
                DataInputStream fis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                savePath += fis.readUTF();
                DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savePath)));
                long file_len = fis.readLong();
                System.out.println("文件的长度为:" + file_len + "\n");
                System.out.println("开始接收文件!" + "\n");

                int len = 0;
                long length = 0;
                byte[] b1 = new byte[1024];
                while((len = fis.read(b1)) != -1) {
                    fileOut.write(b1, 0, len);
                    length += len;
                    if(length >= file_len) {
                        break;
                    }
                }
                System.out.println("接收完成，文件存为" + savePath + "\n");
                fileOut.close();
                    long startTime=System.currentTimeMillis();
                st.setImage(savePath);
                String result = st.stu();
                    long endTime=System.currentTimeMillis();
                    System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
                //ClientSocket cs = new ClientSocket(s.getInetAddress().getHostAddress(), 8822);
                //cs.CreateConnection();
                //cs.sendMessage(result);
                PrintWriter printWriter = new PrintWriter(s.getOutputStream());
                printWriter.write(result);
                printWriter.flush();
                printWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }


    }

    public static void main(String arg[]) {
        new ImageReceiver().start();
    }
}