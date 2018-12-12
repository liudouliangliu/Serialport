package com.serialport;

import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @auhor : lhj
 * @date : 2018/11/28 13:15
 * @des :
 */
public class SeriaUtil {
    private static final String TAG = "SerialPort";
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    String path = "/dev/ttyS4";
    int port = 9600;
    int flag = 0;

    private static SeriaUtil instance;

    static {
        System.loadLibrary("seriaLib");
    }


    public static SeriaUtil getInstance(){
        if (instance==null){
            synchronized (SeriaUtil.class){
                if (instance==null){
                    instance = new SeriaUtil();
                }
            }
        }
        return instance;
    }

    private SeriaUtil(){
        mFd = open(path, port, flag);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            return;
        }
        mFileOutputStream = new FileOutputStream(mFd);
    }
    // Getters and setters

    public OutputStream getOutputStream() {
        if (mFileOutputStream == null){
            mFd = open(path, port, flag);
            if (mFd == null) {
                Log.e(TAG, "native open returns null");
                return null;
            }
            mFileOutputStream = new FileOutputStream(mFd);
        }
        return mFileOutputStream;
    }
    /**
     * 数据位：8位
     * 停止位：1位
     * 效验位：None
     * 串口号：ttyS4
     * 通信标准：9600波特率
     * @param path  ttyS4
     * @param baudrate 9600波特率
     * @param flag
     * @return
     */

    private native FileDescriptor open( String path, int baudrate, int flag);

    private native void close();

    public void closeSeria(){
        if (mFileOutputStream!=null){
            try {
                mFileOutputStream.close();
                mFileOutputStream = null;
                if (mFd!=null){
                    close();
                }
                instance = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
