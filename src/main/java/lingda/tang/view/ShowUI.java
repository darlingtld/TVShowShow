package lingda.tang.view;

import lingda.tang.pojo.DownloadLink;
import lingda.tang.pojo.Show;
import lingda.tang.provider.MeijuttProvider;
import lingda.tang.service.InstanceHolder;
import lingda.tang.service.LocalFileService;
import lingda.tang.util.Strings;
import lingda.tang.util.Utils;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.event.*;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by darlingtld on 2015/1/15.
 */
public class ShowUI {

    static ShowUI showUI;
    static JFrame manageShowFrame = new JFrame("ManageShowUI");
    private static Map<String, List<DownloadLink>> linksMap = new HashMap<String, List<DownloadLink>>();
    JFrame searchFrame = new JFrame("SearchUI");
    private MeijuttProvider mjttProvider = InstanceHolder.getMjttProvider();
    private LocalFileService localFileService = InstanceHolder.getLocalFileService();
    private JTextField searchForm;
    private JButton bSearch;
    private JTabbedPane showTabbedPane;
    private JRadioButton hdRadioButton;
    private JRadioButton normalRadioButton;
    private JPanel showPanel;
    private JButton updateAllButton;
    private JProgressBar progressBar;

    public ShowUI() {
        udpateTabbedPane();

        searchForm.setText(Strings.SEARCH_SHOW_MSG);

        searchForm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (searchForm.getText().trim().equals(Strings.SEARCH_SHOW_MSG)) {
                    searchForm.setText("");
                }
            }
        });
        searchForm.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if ("".equals(searchForm.getText().trim())) {
                    searchForm.setText(Strings.SEARCH_SHOW_MSG);
                }
            }
        });
        updateAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        progressBar.setValue(0);
                        updateAllButton.setEnabled(false);
                        updateAll();
                        updateAllButton.setEnabled(true);
                    }
                }.start();

            }
        });
        bSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Show> showList = mjttProvider.searchShows(searchForm.getText(), progressBar, 1);
                SearchUI searchUI = new SearchUI(searchForm.getText(), showList, showUI);
                searchFrame.setContentPane(searchUI.searchPanel);
                searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                searchFrame.pack();
                searchFrame.setLocationRelativeTo(null);
                searchFrame.setVisible(true);
            }
        });
    }


    public void go(final MainUI mainUI) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final JFrame mainFrame = new JFrame("ShowUI");
                showUI = new ShowUI();
                mainFrame.setContentPane(showUI.showPanel);
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
                JMenuBar menubar = new JMenuBar();
                JMenu settingMenu = new JMenu("设置");
                JMenuItem loadShowMenuItem = new JMenuItem("加载配置文件");
//                JMenuItem showRepositoryMenuItem = new JMenuItem("本地库");
                JMenuItem backToMainUIMenuItem = new JMenuItem("回到主界面");
                backToMainUIMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainUI.setVisible(true);
                        mainFrame.dispose();
                    }
                });
                settingMenu.add(backToMainUIMenuItem);
//                showRepositoryMenuItem.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        final ManageShowUI manageShowUI = new ManageShowUI();
//                        manageShowFrame.setContentPane(manageShowUI.panel1);
//                        manageShowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                        manageShowFrame.pack();
//                        manageShowFrame.setLocationRelativeTo(null);
//                        manageShowFrame.setVisible(true);
//                        manageShowFrame.addWindowListener(new WindowAdapter() {
//                            @Override
//                            public void windowClosing(WindowEvent e) {
//                                for (String showName : manageShowUI.getShowsToDeleteList()) {
//                                    Utils.deleteFromShowList(showName);
//                                }
//                                showUI.udpateTabbedPane();
//                            }
//                        });
//                    }
//                });
//                settingMenu.add(showRepositoryMenuItem);
                loadShowMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread() {
                            @Override
                            public void run() {
                                JFileChooser fc = new JFileChooser();
                                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                                int returnVal = fc.showOpenDialog(null);
                                if (returnVal == JFileChooser.APPROVE_OPTION) {
                                    StringBuffer writeBackStr = new StringBuffer();
                                    File file = fc.getSelectedFile();
                                    try {
                                        int lineCount = Utils.getFileLines(file);
                                        BufferedReader br = new BufferedReader(new FileReader(file));
                                        String line;
                                        while ((line = br.readLine()) != null) {
                                            String[] strArr = line.split(",");
                                            List<Show> showList = mjttProvider.searchShows(strArr[0].trim(), progressBar, lineCount);
                                            Show finalShow = null;
                                            for (Show show : showList) {
                                                try {
                                                    if (show.getSeason() == Integer.parseInt(strArr[1].trim())) {
                                                        finalShow = show;
                                                        break;
                                                    }
                                                } catch (Exception e2) {
                                                    finalShow = showList.get(0);
                                                    break;
                                                }
                                            }
                                            if (null == finalShow) {
                                                JOptionPane.showMessageDialog(null, String.format("%s 第%s季未找到\n请重新修改文件", strArr[0].trim(), strArr[1].trim()));
                                                break;
                                            }
                                            writeBackStr.append(String.format("%s,%s\r\n", finalShow.getEnglishName(), finalShow.getSeason()));
                                            Utils.addToShowList(finalShow.getEnglishName(), finalShow);
                                            Utils.log(finalShow.toString());
                                        }
                                        br.close();
                                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                                        bw.write(writeBackStr.toString());
                                        bw.flush();
                                        bw.close();
                                        showUI.udpateTabbedPane();
                                    } catch (FileNotFoundException e1) {
                                        JOptionPane.showMessageDialog(null, "文件未找到");
                                    } catch (Exception e1) {
                                        JOptionPane.showMessageDialog(null, "文件读写错误,请检查文件");
                                    }
                                    Utils.log("Opening: " + file.getName() + ".");
                                } else {
                                    Utils.log("Open command cancelled by user.");
                                }
                            }
                        }.start();
                    }
                });
                settingMenu.add(loadShowMenuItem);
                menubar.add(settingMenu);
                mainFrame.setJMenuBar(menubar);
            }
        });
    }


    public void udpateTabbedPane() {
        showTabbedPane.removeAll();
//        localFileService.init();
        for (Show show : Utils.getShowList()) {
            showTabbedPane.addTab(String.format("%s 第%s季", show.getName(), Utils.convertNumberToChinese(show.getSeason())), new JPanel());
        }
    }

    private void updateAll() {
        Utils.SetProgress(progressBar, 0);
        boolean isHiDefinitonChecked = hdRadioButton.isSelected();
        final AtomicInteger index = new AtomicInteger(0);
        for (int i = 0; i < Utils.getShowList().size(); i++) {
            Show show = Utils.getShowList().get(i);
            String showName = show.getEnglishName();
            try {
                linksMap.put(showName, mjttProvider.fetchDownloadLinks(showName, show.getSeason()));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            JPanel panel = (JPanel) showTabbedPane.getComponentAt(i);
            panel.removeAll();
            JTextPane textPane = new JTextPane();
            textPane.setEditable(false);
            textPane.setContentType("text/html");
            textPane.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        JOptionPane.showMessageDialog(null, Strings.COPY_LINK_TO_THUNDER);
                        Utils.setSysClipboardText(e.getDescription());
                    }

                }
            });
            StringBuffer sb = new StringBuffer();
            List<DownloadLink> downloadLinkList = linksMap.get(showName);
            Collections.reverse(downloadLinkList);
            for (DownloadLink link : downloadLinkList) {
                if (link.isHighDefinition() == isHiDefinitonChecked) {
                    sb.append(String.format("<font color='blue' size='6'>影视名称 ：%s\t%s\t第%s季第%s集\t%s</font>", link.getShowName(), show.getEnglishName(), link.getSeason(), link.getEpisode(), link.isHighDefinition() ? "高清" : "普通")).append("<br>");
                    sb.append(String.format("<font color='gray' size='4'>下载地址 : </font><a href='%s'>%s</a>", link.getUrl(), link.getUrl())).append("<br><br>");
                }
            }
            if (sb.length() == 0) {
                textPane.setText(Strings.NO_DOWNLOAD_LINKS_FOUND);
            } else {
                textPane.setText(sb.toString());
            }
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(textPane);
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
            panel.setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            );

            new Thread() {
                @Override
                public void run() {
                    int value = (index.incrementAndGet() * 100) / Utils.getShowList().size();
                    Utils.SetProgress(progressBar, value);
                    progressBar.updateUI();
                }
            }.start();

            panel.updateUI();
        }
    }
}
