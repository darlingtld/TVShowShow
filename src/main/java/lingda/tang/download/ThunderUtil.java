package lingda.tang.download;

/**
 * Created by darlingtld on 2015/2/27.
 */
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.NativeLongByReference;


public class ThunderUtil {

    static {
        // 注册dll
        Native.register("D:\\Program Files\\Thunder\\Program\\DownloadClient");
    }

    // --------XLError.h中的状态--------------
    public static final int XL_ERROR_FAIL = 268435456;
    public static final int XL_SUCCESS = 0;
    public static final int XL_ERROR_FILE_NAME_INVALID = XL_ERROR_FAIL + 12;
    public static final int XL_ERROR_ADD_TASK_TRAY_ICON_FAIL = XL_ERROR_FAIL + 4;
    public static final int XL_ERROR_FILE_ALREADY_EXIST = XL_ERROR_FAIL + 16;
    public static final int XL_ERROR_INVALID_TASK_TYPE = XL_ERROR_FAIL + 15;
    public static final int XL_ERROR_BUFFER_TOO_SMALL = XL_ERROR_FAIL + 22;
    public static final int XL_ERROR_CANNOT_CONTINUE_TASK = XL_ERROR_FAIL + 20;
    public static final int XL_ERROR_INIT_TASK_TRAY_ICON_FAIL = XL_ERROR_FAIL + 3;
    public static final int XL_ERROR_INVALID_ARG = XL_ERROR_FAIL + 10;
    public static final int XL_ERROR_CREATE_DIRECTORY_FAIL = XL_ERROR_FAIL + 8;
    public static final int XL_ERROR_STRING_IS_EMPTY = XL_ERROR_FAIL + 6;
    public static final int XL_ERROR_TASK_DONT_EXIST = XL_ERROR_FAIL + 11;
    public static final int XL_ERROR_UNINITAILIZE = XL_ERROR_FAIL + 1;
    public static final int XL_ERROR_POINTER_IS_NULL = XL_ERROR_FAIL + 5;
    public static final int XL_ERROR_CANNOT_PAUSE_TASK = XL_ERROR_FAIL + 21;
    public static final int XL_ERROR_PATH_DONT_INCLUDE_FILENAME = XL_ERROR_FAIL + 7;
    public static final int XL_ERROR_WRITE_CFG_FILE_FAIL = XL_ERROR_FAIL + 19;
    public static final int XL_ERROR_INIT_THREAD_EXIT_TOO_EARLY = XL_ERROR_FAIL + 23;
    public static final int XL_ERROR_READ_CFG_FILE_FAIL = XL_ERROR_FAIL + 18;
    public static final int XL_ERROR_NOTIMPL = XL_ERROR_FAIL + 13;
    public static final int XL_ERROR_MEMORY_ISNT_ENOUGH = XL_ERROR_FAIL + 9;
    public static final int XL_ERROR_FILE_DONT_EXIST = XL_ERROR_FAIL + 17;
    public static final int XL_ERROR_UNSPORTED_PROTOCOL = XL_ERROR_FAIL + 2;
    public static final int XL_ERROR_TASKNUM_EXCEED_MAXNUM = XL_ERROR_FAIL + 14;

    // -------XLDownload中Task的状态------------
    /**
     * 开始建立连接
     */
    public static final int XL_STATUS_CONNECT = 0;

    /**
     * 开始下载
     */
    public static final int XL_STATUS_DOWNLOAD = 2;

    /**
     * 暂停
     */
    public static final int XL_STATUS_PAUSE = 10;

    /**
     * 下载成功
     */
    public static final int XL_STATUS_SUCESS = 11;

    /**
     * 下载失败
     */
    public static final int XL_STATUS_FAIL = 12;

    // ------XLDownload中的方法签名---------------------

    /**
     * 初始化引擎
     * @return TRUE，表示成功；FALSE，表示失败。
     */
    public static native boolean XLInitDownloadEngine();

    /**
     * 下载指定的资源，并保存到本地文件，只支持HTTP，不支持动态链
     * @param pszFileName 下载资源的本地文件名
     * @param pszUrl 资源的URL，不能为NULL
     * @param pszRefUrl 资源的引用页，可以为NULL
     * @param lTaskId 【传出参数】唯一标识引擎创建的任务，该参数可以作为XLQueryTaskInfo、XLStopTask的第一个参数。如果该参数值为0，表示引擎创建任务失败
     * @return XL_SUCCESS，表示成功。其他值，表示失败
     */
    public static native int XLURLDownloadToFile(WString pszFileName, WString pszUrl, WString pszRefUrl, NativeLongByReference lTaskId);

    /**
     * 查询指定任务的当前状态
     * @param lTaskId 指定将要查询的任务。通过调用XLURLDownloadToFile可以获取一个任务ID
     * @param plStatus 【传出参数】返回任务的当前状态。可能的取值，参见XLDownload.h
     * @param pullFileSize 【传出参数】文件大小，单位是字节
     * @param pullRecvSize 【传出参数】已经下载到的数据，单位是字节
     * @return
     */
    public static native int XLQueryTaskInfo(NativeLong lTaskId, NativeLongByReference plStatus, LongByReference pullFileSize, LongByReference pullRecvSize);

    /**
     * 暂停指定任务，并返回新的任务ID
     * @param lTaskId 任务ID。通过调用XLURLDownloadToFile可以获取一个任务ID
     * @param lNewTaskId 【传出参数】当返回值等于XL_SUCCESS时，返回新的任务ID；否则，值未定义
     * @return XL_SUCCESS，表示成功；其他值，表示失败。
     */
    public static native int XLPauseTask(NativeLong lTaskId, NativeLongByReference lNewTaskId);

    /**
     * 恢复已暂停的任务
     * @param lTaskId 任务ID
     * @return XL_SUCCESS，表示成功；其他值，表示失败。
     */
    public static native int XLContinueTask(NativeLong lTaskId);

    /**
     * 从指定的TD文件开始新任务
     * @param pszTdFileFullPath TD文件的完整路径及文件名
     * @param lTaskId 【传出参数】调用成功时，返回新建任务的ID
     * @return XL_SUCCESS，表示成功；其他，表示失败。详见XLError.h
     */
    public static native int XLContinueTaskFromTdFile(String pszTdFileFullPath, NativeLongByReference lTaskId);

    /**
     * 停止指定任务
     * 【注意】每个任务，无论下载成功或失败，最后都必须调用一次XLStopTask。否则，会导致资源泄漏。
     * @param lTaskId 要停止的任务ID
     */
    public static native void XLStopTask(NativeLong lTaskId);

    /**
     * 进行一些资源回收操作
     * 【注意】当不再使用引擎时，必须调用该函数，否则会导致资源泄漏。
     * @return TRUE，表示成功；FALSE，表示失败。
     */
    public static native boolean XLUninitDownloadEngine();

    /**
     * 将错误码对应的错误消息拷贝至指定的缓冲区。
     * @param dwErrorId 开放下载引擎定义的错误码，详见XLError.h
     * @param pszBuffer 缓冲区首地址
     * @param dwSize 【传出参数】传入时，指定缓冲区长度；函数返回时，如果缓冲区长度不足，该参数被设置成所需的缓冲区长度。该参数的单位是字符数，不是字节数。
     * @return
     */
    public static native int XLGetErrorMsg(int dwErrorId, String pszBuffer, IntByReference dwSize);


    //-------------------工具函数----------------

    /**
     * long转NativeLongByReference
     * @param value
     * @return
     */
    public static NativeLongByReference makeNativeRefLong(long value) {
        NativeLongByReference ullFileSize = new NativeLongByReference();
        ullFileSize.setValue(new NativeLong(value));
        return ullFileSize;
    }

    /**
     * NativeLongByReference转long
     * @param longRef
     * @return
     */
    public static long getLongValue(NativeLongByReference longRef) {
        return longRef.getValue().longValue();
    }


    // 测试代码：  http://xldoc.xl7.xunlei.com/0000000026/index.html的C语言的Java翻版
    public static void main( String[] args) throws InterruptedException {
        int result;

        try {
            if (!XLInitDownloadEngine()) {
                System.out.println("Initialize download engine failed.");
                return;
            }

            NativeLongByReference lTaskId = makeNativeRefLong(0);
            result = XLURLDownloadToFile(
                    new WString("d:\\test.mkv"),
                    new WString("thunder://QUFlZDJrOi8vfGZpbGV8JUU1JUE0JThEJUU0JUJCJTg3LlJldmVuZ2UuUzA0RTE0LiVFNCVCOCVBRCVFOCU4QiVCMSVFNSVBRCU5NyVFNSVCOSU5NS5XRUItSFIuQUMzLjEwMjRYNTc2LngyNjQubWt2fDUxMTI1MjU5N3w3MTBmZjA3NGQ2NWVkZDYwZjFjODc0YTkyZGEzZTk4ZHxoPXA1eW1ldTd3ZXNuZ3ZjY2NtemJpeDc0eXU1NDdmdmlhfC9aWg="),
                    new WString(""),
                    lTaskId);
            System.out.println("result code: " + result);
            System.out.println("task id: " + getLongValue(lTaskId));
            if (XL_SUCCESS != result) {
                XLUninitDownloadEngine();
                System.out.println("Create new task failed, error code: " + ( result - XL_ERROR_FAIL) );
                return;
            }
            System.out.println("Begin download file.");

            do {
                Thread.sleep(1000);

                LongByReference ullFileSize = new LongByReference(0);
                LongByReference ullRecvSize = new LongByReference(0);
                NativeLongByReference lStatus = makeNativeRefLong(-1);
                NativeLong nativeLong = new NativeLong();
                nativeLong.setValue(getLongValue(lTaskId));

                result = XLQueryTaskInfo(nativeLong, lStatus, ullFileSize, ullRecvSize);
                if (XL_SUCCESS == result) {
                    // 输出进度信息
                    if (0 != ullFileSize.getValue()) {
                        double douProgress = (double) ullRecvSize.getValue() / (double) ullFileSize.getValue();
                        douProgress *= 100.0;
                        System.out.println("Download progress:" + douProgress);
                    } else {
                        System.out.println("File size is zero.");
                    }

                    if (XL_STATUS_SUCESS == getLongValue(lStatus)) {
                        System.out.println("Download successfully.\n");
                        break;
                    }

                    if (XL_STATUS_FAIL == getLongValue(lStatus)) {
                        System.out.println("Download failed.");
                        break;
                    }
                }
            } while (XL_SUCCESS == result);
            XLStopTask(lTaskId.getValue());
        } finally {
            XLUninitDownloadEngine();
        }

    }
}
