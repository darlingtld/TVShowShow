package lingda.tang.download;

/**
 * Created by darlingtld on 2015/2/27.
 */
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * BASE64编码
 * @author jiangsyace
 * @date 2014-07-29
 */
public class JavaBase64 {

    public static void main(String[] args) {

        String resource = "http://192.168.56.1:8080/portal2.7z";

        //迅雷的编码规则为：原地址前面加"AA"，后面加"ZZ"，然后进行Base64编码，最后加上迅雷下载协议"Thunder://"组成完整的下载链接
        String resourceForThunder = "Thunder://" + getBASE64("AA" + resource + "ZZ");
        //快车的编码规则：原地址前后都加上"[FLASHGET]"，注意后面还要加上"&"，符号怎么得出不清楚，在最后面加的是其他个人信息，至今未有人报告转换错误
        String resourceForFlashget = "Flashget://" + getBASE64("[FLASHGET]" + resource + "[FLASHGET]") + "&something";
        //旋风相对就简单多了：将原地址直接base64编码加上下载协议就好了
        String resourceForQQDownLoad = "qqdl://" + getBASE64(resource);

        System.out.println(resourceForThunder);
        System.out.println(resourceForFlashget);
        System.out.println(resourceForQQDownLoad);

        //解码调用getFromBASE64，注意不要将resourceForThunder直接进行解码，因为当中含有"Thunder://"等未经过编码的字符
        System.out.println("迅雷链接解码:"+getFromBASE64("QUFodHRwOi8vMTkyLjE2OC41Ni4xOjgwODAvcG9ydGFsMi43elpa"));

    }

    //BASE64 编码
    public static String getBASE64(String s) {
        if (s == null)
            return null;
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(s.getBytes());

    }

    //BASE64 解码
    public static String getFromBASE64(String s) {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }

}