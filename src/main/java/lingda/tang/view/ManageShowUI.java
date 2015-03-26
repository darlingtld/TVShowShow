package lingda.tang.view;

import lingda.tang.util.Strings;
import lingda.tang.util.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darlingtld on 2015/1/20.
 */
public class ManageShowUI {
    public JPanel panel1;
    private final int MAX = 5;
    private final int TOTAL_PAGE = (Utils.getShowList().size() - 1) / MAX + 1;
    private int currentPage = 0;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JButton bPageUp;
    private JButton bPageDown;
    private JLabel curPageLabel;
    private List<JLabel> labelList = new ArrayList<JLabel>();
    private List<JButton> buttonList = new ArrayList<JButton>();

    public List<String> getShowsToDeleteList() {
        return showsToDeleteList;
    }

    private List<String> showsToDeleteList = new ArrayList<String>();

    public ManageShowUI() {
        labelList.add(label1);
        labelList.add(label2);
        labelList.add(label3);
        labelList.add(label4);
        labelList.add(label5);
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
        buttonList.add(button5);

        updateAll();

        bPageDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPage++;
                updateAll();
            }
        });

        bPageUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPage--;
                updateAll();
            }
        });

        for (int i = 0; i < buttonList.size(); i++) {
            final int finalI = i;
            buttonList.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonList.get(finalI).setText("已" + Strings.DELETE_SHOW);
                    buttonList.get(finalI).setEnabled(false);
                    Utils.log("delete %s showlist size %s", currentPage * MAX + finalI, Utils.getShowList().size());
                    showsToDeleteList.add(Utils.getShowList().get(currentPage * MAX + finalI).getEnglishName());
                }
            });
        }

    }

    private void updateAll() {
        int min = MAX < Utils.getShowList().size() - currentPage * MAX ? MAX : Utils.getShowList().size() - currentPage * MAX;
        for (int i = 0; i < min; i++) {
            buttonList.get(i).setVisible(true);
            labelList.get(i).setVisible(true);
            buttonList.get(i).setText(Strings.DELETE_SHOW);
            labelList.get(i).setText(String.format("%s   %s", Utils.getShowList().get(i + currentPage * MAX).getEnglishName(), Utils.getShowList().get(i + currentPage * MAX).getName()));
        }
        curPageLabel.setText(String.format("第%s页", currentPage + 1));
        if (currentPage <= 0) {
            bPageUp.setEnabled(false);
        } else {
            bPageUp.setEnabled(true);
        }
        if (currentPage >= TOTAL_PAGE - 1) {
            bPageDown.setEnabled(false);
        } else {
            bPageDown.setEnabled(true);
        }
        for (int i = min; i < MAX; i++) {
            buttonList.get(i).setVisible(false);
            labelList.get(i).setVisible(false);
        }

    }
}
