package contrast.heaven.com.serialport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.serialport.SeriaUtil;

import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnOpen, mBtnSend;
    private TextView mTv;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.sample_text);
        mBtnOpen = findViewById(R.id.btn_open);
        mBtnSend = findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnOpen.setOnClickListener(this);
    }

    @Override
    public void onClick( View view ) {
        switch (view.getId()) {
            case R.id.btn_open:
                //构造方法中有打开串口的方法
                SeriaUtil.getInstance();
                break;
            case R.id.btn_send:
                try {
                    byte[] bytes = {0x55, 0x02, 0x00, 0x02, 0x00};
                    OutputStream outputstream = SeriaUtil.getInstance().getOutputStream();
                    if (outputstream != null) {
                        outputstream.write(bytes);
                        outputstream.write('\n');
                        outputstream.flush();
                        mTv.setText("发送数据成功");
                    }
                } catch (Exception e) {
                    mTv.setText("发送数据异常==" + e.toString());
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onStop();
        closeSerialPort();
        Log.i("tag", "执行了onDestroy");
    }

    private void closeSerialPort() {
        SeriaUtil.getInstance().closeSeria();
    }
}
