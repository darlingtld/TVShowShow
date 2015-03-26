package lingda.tang.download;

/**
 * Created by darlingtld on 2015/2/27.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextXunLeiFrame extends JFrame {// 模拟迅雷下载的界面面板
    private JPanel contentPane;// 迅雷面板
    private JTextField webField = new JTextField();// 下载地址的文本框
    private JTextField localFile = new JTextField();// 下载到本地的文本框
    private JButton button = new JButton();// 下载按钮
    private JLabel webLabel = new JLabel();// 目标标签
    private JLabel localLabel = new JLabel();// 下载到本地标签
    private JTextArea textArea = new JTextArea();// 显示下载记录的文本域
    private String downloadURL = new String();// 下载地址
    private String saveFileAs = new String();// 另存为

    public TextXunLeiFrame() {// 构造方法进行初始化
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            toInit();// 调用方法初始化面板
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void toInit() throws Exception {// 初始化面板
        contentPane = (JPanel) this.getContentPane();// 创建面板
        contentPane.setLayout(null);// 没有设置面板布局
        this.setSize(new Dimension(380, 320));// 面板的大小
        this.setLocation(100, 100);// 面板位置
        this.setTitle("仿迅雷多线程下载");// 面板标题
        webField.setBounds(new Rectangle(150, 200, 200, 20));// 设置文本框的位置
        // 设置默认下载路径
        webField.setText("http://image.xsoftlab.net/wx.jpg");
        localFile.setBounds(new Rectangle(150, 240, 120, 20));// 设置文本框的位置
        localFile.setText("d:\\wx.jpg");// 设置默认另存为
        webLabel.setBounds(new Rectangle(20, 200, 120, 20));// 标签的位置
        webLabel.setText("下载的目标文件为： ");
        localLabel.setBounds(new Rectangle(20, 240, 120, 20));// 标签的位置
        localLabel.setText("下载的文件另存为： ");
        button.setBounds(new Rectangle(280, 240, 60, 20));// 按钮的位置
        button.setText("下载");
        button.addActionListener(new ActionListener() {// 按钮添加监听事件
            public void actionPerformed(ActionEvent e) {
                button_actionPerformed(e);// 调用事件
            }
        });
        JScrollPane scrollPane = new JScrollPane(textArea);// 创建有滑动条的面板将文本域放在上面
        scrollPane.setBounds(new Rectangle(20, 20, 330, 170));// 面板的位置
        textArea.setEditable(false);// 不可编辑
        contentPane.add(webField, null);// 将文本框添加到面板中
        contentPane.add(localFile, null);// 将文本框添加到面板中
        contentPane.add(webLabel, null);// 将标签添加到面板中
        contentPane.add(localLabel, null);// 将标签添加到面板中
        contentPane.add(button, null);// 将按钮添加到面板中
        contentPane.add(scrollPane, null);// 将滑动条添加到面板中
        downloadURL = webField.getText();// 获得文本框中的文本
        saveFileAs = localFile.getText();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);// 设置默认关闭操作
    }

    public void button_actionPerformed(ActionEvent e) {// 点击事件触发方法，启动分析下载文件的进程
        downloadURL = webField.getText();// 获得目标文件的网址
        saveFileAs = localFile.getText();// 获得另存为的地址
        if (downloadURL.compareTo("") == 0)
            textArea.setText("请输入要下载的文件完整地址");
        else if (saveFileAs.compareTo("") == 0) {
            textArea.setText("请输入保存文件完整地址");
        } else {
            try {
                DownloadFile downFile = new DownloadFile(downloadURL,
                        saveFileAs, 5, textArea);// 传入参数实例化下载文件对象
                downFile.start();// 启动下载文件的线程
                textArea.append("主线程启动...");
            } catch (Exception ec) {// 捕获异常
                System.out.println("下载文件出错：" + ec.getMessage());
            }

        }

    }

    public static void main(String[] args) {// java程序主入口处
        TextXunLeiFrame frame = new TextXunLeiFrame();// 实例化对象进行初始化
        frame.setVisible(true);// 设置窗口可视
    }
}
