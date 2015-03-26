package lingda.tang.service;

import lingda.tang.pojo.Show;

import java.util.Comparator;

/**
 * Created by darlingtld on 2015/2/6.
 */
public class ShowComparator implements Comparator<Show> {
    private String keyword;

    public ShowComparator(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    @Override
    public int compare(Show o1, Show o2) {
        if (o1.getName().equals(o2.getName())) return 0;
        if (o1.getName().toLowerCase().contains(keyword) && !o2.getName().toLowerCase().contains(keyword)) return -1;
        if (!o1.getName().toLowerCase().contains(keyword) && o2.getName().toLowerCase().contains(keyword)) return 1;
        if (o1.getName().toLowerCase().contains(keyword) && o2.getName().toLowerCase().contains(keyword)) {
            return o1.getName().toLowerCase().indexOf(keyword) - o2.getName().toLowerCase().indexOf(keyword);
        } else {
            int sum = 0;
            for (int i = 0; i < keyword.length(); i++) {
                if (o1.getName().toLowerCase().contains(keyword.substring(i, i + 1))) {
                    sum--;
                }
                if (o2.getName().toLowerCase().contains(keyword.substring(i, i + 1))) {
                    sum++;
                }
            }
            return sum;
        }
    }
}
