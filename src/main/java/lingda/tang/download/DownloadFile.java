package lingda.tang.download;

/**
 * Created by darlingtld on 2015/2/27.
 */

import javax.swing.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFile extends Thread {// 分析下载的文件并启动下载进程
    String downloadURL;// 下载文件的地址
    String saveFileAs;// 文件另存为
    int threadCount;// 线程总数
    String log = new String();// 下载过程的日志记录
    JTextArea textArea = new JTextArea();// 创建文本域
    long[] position;
    long[] startPosition;// 每个线程开始位置
    long[] endPosition;// 每个线程结束位置
    FileSplit[] FileSplitt; // 子线程对象
    long fileLength;// 下载的文件的长度

    public DownloadFile(String downloadURL, String saveFileAs, int threadCount,
                        JTextArea textArea) {// 构造方法进行初始化
        this.downloadURL = downloadURL;
        this.saveFileAs = saveFileAs;
        this.threadCount = threadCount;
        this.textArea = textArea;
        startPosition = new long[threadCount];
        endPosition = new long[threadCount];
    }

    public void run() {// 实现Thread类的方法
        log = "目标文件： " + downloadURL;
        textArea.append("\n" + log);// 日志写入文本域
        log = "\n 线程总数： " + threadCount;
        textArea.append("\n" + log);

        try {
            fileLength = getFileSize();// 获得文件长度
            if (fileLength == -1) {// 不可获取文件长度或没有找到资源
                textArea.append("\n 不可知的文件长度！请重试！！");
            } else {
                if (fileLength == -2) {// 无法获取文件或没有找到资源
                    textArea.append("\n 文件无法获取,没有找到指定资源,请重试！！");
                } else {
                    for (int i = 0; i < startPosition.length; i++) {// 循环对每个线程的开始位置赋值
                        startPosition[i] = (long) (i * (fileLength / startPosition.length));
                    }
                    for (int i = 0; i < endPosition.length - 1; i++)
                        // 循环对每个线程的结束位置赋值
                        endPosition[i] = startPosition[i + 1];
                    endPosition[endPosition.length - 1] = fileLength;// 最后一个线程的结束位置是文件的长度
                    for (int i = 0; i < startPosition.length; i++) {// 循环显示每个线程的开始和结束位置
                        log = "线程：" + i + "下载范围：" + startPosition[i] + "--"
                                + endPosition[i];
                        textArea.append("\n" + log);
                    }
                    FileSplitt = new FileSplit[startPosition.length];
                    for (int i = 0; i < startPosition.length; i++) {// 启动一组子线程
                        FileSplitt[i] = new FileSplit(downloadURL, saveFileAs,
                                startPosition[i], endPosition[i], i, textArea);
                        log = "线程 " + i + "启动";
                        textArea.append("\n" + log);
                        FileSplitt[i].start();// 启动线程
                    }

                    boolean breakWhile = true;
                    while (breakWhile) {// 当条件始终为true时进行循环
                        Thread.sleep(500);// 线程休眠
                        breakWhile = false;
                        for (int i = 0; i < FileSplitt.length; i++) {
                            if (!FileSplitt[i].isDone) {// 循环判断每个线程是否结束
                                breakWhile = true;
                                break;
                            }
                        }
                    }
                    textArea.append("\n 文件传输结束！");// 文件传输结束
                }
            }
        } catch (Exception ex) {// 捕获异常
            ex.printStackTrace();
        }

    }

    public long getFileSize() {// 获得文件的长度的方法
        int fileLength = -1;
        try {
            URL url = new URL(downloadURL);// 根据网址创建URL对象
            HttpURLConnection httpConnection = (HttpURLConnection) (url
                    .openConnection());// 创建远程对象连接对象
            int responseCode = httpConnection.getResponseCode();
            if (responseCode >= 400) {// 没有获得响应信息
                System.out.println("Web服务器响应错误");
                return -2;// Web服务器响应错误
            }
            String sHeader;
            for (int i = 1; ; i++) { // 查找标识文件长度的文件头，获取文件长度
                sHeader = httpConnection.getHeaderFieldKey(i);
                if (sHeader != null) {
                    if (sHeader.equals("Content-Length")) {// 查找标识文件长度的文件头
                        fileLength = Integer.parseInt(httpConnection
                                .getHeaderField(sHeader));
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {// 捕获异常
            System.out.println("无法获得文件长度:" + e.getMessage());
        }
        return fileLength;

    }
}
