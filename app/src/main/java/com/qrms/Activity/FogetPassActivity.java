package com.qrms.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.qrms.R;

public class FogetPassActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText_Login;
    Button button_submit;
    TextView text_Cancel;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_pass);

        initObjects();
        text_Cancel.setPaintFlags(text_Cancel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // ::::::::::::::: OnclickListner ::::::::::::::::::::::
        button_submit.setOnClickListener(this);
        text_Cancel.setOnClickListener(this);
    }
    public void cancel(View view){
        finish();
    }


    private void  initObjects()
    {
        editText_Login=(EditText)findViewById(R.id.editText_Login);
        text_Cancel=(TextView) findViewById(R.id.text_Cancel);
        button_submit=(Button) findViewById(R.id.button_submit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_submit:
                String emailId=editText_Login.getText().toString().trim();
                if (editText_Login.getText().toString().isEmpty()) {
                    Toast.makeText(FogetPassActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                    editText_Login.startAnimation(shake);
                }
                else if(!emailId.matches(emailPattern))
                {
                    Toast.makeText(FogetPassActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                    editText_Login.startAnimation(shake);
                }
                else
                {
                    Toast.makeText(FogetPassActivity.this,"Working Progress",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_Cancel:
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;
        }
    }
}