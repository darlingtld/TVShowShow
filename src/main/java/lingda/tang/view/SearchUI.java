package lingda.tang.view;

import lingda.tang.pojo.Show;
import lingda.tang.util.Strings;
import lingda.tang.util.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darlingtld on 2015/1/16.
 */
public class SearchUI {
    private JLabel labelSearchResult;
    public JPanel searchPanel;
    private JLabel label1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;

    public SearchUI(String searchWord, final List<Show> showList, final ShowUI showUI) {
        final List<JLabel> labelList = new ArrayList<JLabel>();
        labelList.add(label1);
        labelList.add(label2);
        labelList.add(label3);
        labelList.add(label4);
        labelList.add(label5);
        List<JButton> buttonList = new ArrayList<JButton>();
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
        buttonList.add(button5);
        labelSearchResult.setText(labelSearchResult.getText() + searchWord);
        int min = labelList.size() < showList.size() ? labelList.size() : showList.size();
        for (int i = 0; i < min; i++) {
            labelList.get(i).setText(String.format("%s\t第%s季", showList.get(i).getName(), Utils.convertNumberToChinese(showList.get(i).getSeason())));
        }
        for (int i = min; i < labelList.size(); i++) {
            labelList.get(i).setVisible(false);
            buttonList.get(i).setVisible(false);
        }
        for (int i = 0; i < buttonList.size(); i++) {
            final int finalI = i;
            buttonList.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Utils.addToShowList(showList.get(finalI).getEnglishName(), showList.get(finalI));
                    int option = JOptionPane.showConfirmDialog(null,
                            String.format(Strings.ADD_SHOW_CONFIRMATION_MSG + "\t%s\t%s\t第%s季", showList.get(finalI).getName(), showList.get(finalI).getEnglishName(), Utils.convertNumberToChinese(showList.get(finalI).getSeason())));
                    if (option == JOptionPane.YES_OPTION) {
                        showUI.searchFrame.dispose();
                        showUI.udpateTabbedPane();
                    }
                }
            });
        }

    }
}
