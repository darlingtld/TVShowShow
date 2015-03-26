package lingda.tang.download;

/**
 * Created by darlingtld on 2015/2/27.
 */
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JTextArea;

public class FileSplit extends Thread {
    String downloadURL;// 下载文件的地址
    long startPosition;// 线程的开始位置
    long endPosition;// 线程的结束位置
    int threadID;
    JTextArea textArea = new JTextArea();// 创建文本域
    boolean isDone = false;// 是否下载完毕
    RandomAccessFile random;

    public FileSplit(String downloadURL, String saveAs, long nStart, long nEnd,
                     int id, JTextArea textArea) {
        this.downloadURL = downloadURL;
        this.startPosition = nStart;
        this.endPosition = nEnd;
        this.threadID = id;
        this.textArea = textArea;
        try {
            random = new RandomAccessFile(saveAs, "rw");// 创建随机访问对象，以读/写方式
            random.seek(startPosition);// 定位文件指针到startPosition位置
        } catch (Exception e) {// 捕获异常
            System.out.println("创建随机访问对象出错：" + e.getMessage());
        }
    }

    public void run() {// 实现Thread类的方法
        try {
            URL url = new URL(downloadURL);// 根据网址创建URL对象
            HttpURLConnection httpConnection = (HttpURLConnection) url
                    .openConnection();// 创建远程对象连接对象
            String sProperty = "bytes=" + startPosition + "-";
            httpConnection.setRequestProperty("RANGE", sProperty);
            textArea.append("\n 线程" + threadID + "下载文件!  请等待...");
            InputStream input = httpConnection.getInputStream();// 获得输入流对象
            byte[] buf = new byte[1024];// 创建字节数据存储文件的数据
            int splitSpace;
            splitSpace = (int) endPosition - (int) startPosition;// 获得每个线程的间隔
            if (splitSpace > 1024)
                splitSpace = 1024;
            while (input.read(buf, 0, splitSpace) > 0
                    && startPosition < endPosition) {// 读取文件信息
                splitSpace = (int) endPosition - (int) startPosition;
                if (splitSpace > 1024)
                    splitSpace = 1024;
                textArea.append("\n线程: " + threadID + " 开始位置: " + startPosition
                        + "，  间隔长度: " + splitSpace);
                random.write(buf, 0, splitSpace);// 写入文件
                startPosition += splitSpace;// 开始位置改变
            }
            textArea.append("\n 线程" + threadID + "下载完毕！！");
            random.close();// 释放资源
            input.close();
            isDone = true;
        } catch (Exception e) {// 捕获异常
            System.out.println("多线程下载文件出错：" + e.getMessage());
        }
    }
}
