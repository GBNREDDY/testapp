package com.testapp;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;


import org.json.JSONObject;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    TextView stats;
    LoginButton loginButton;
    CallbackManager callbackManager;
    int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);


        Button button = (Button) findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog(MainActivity.this);
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Test")
                        .setContentDescription(
                                "Test")
                        .setContentUrl(Uri.parse("http://itechways.com")).build();
                shareDialog.show(linkContent);
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

            }
        });

        Calendar c = Calendar.getInstance();

        String[] str = new String[32];
        for (int i = 0; i < 32; i++) {
            str[i] = c.getTime().toString().substring(0, 11);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        initializeControls();
        //String [] str={"1asdasd", "2asdsafaf", "3asdafasfa", "4fafasdasf", "5asfasfasfa", "6assdsdsafas", "7afafac"};
        NumberPicker np = (NumberPicker) findViewById(R.id.np);
        np.setDisplayedValues(str);
        np.setMinValue(0);
        np.setMaxValue(31);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                stats.setText(picker.getDisplayedValues().toString() + "\n" + oldVal + "\n" + newVal);

            }
        });


        NumberPicker np1 = (NumberPicker) findViewById(R.id.np2);
        np1.setMinValue(1);
        np1.setMaxValue(12);
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                stats.setText(picker.getValue() + "\n" + oldVal + "\n" + newVal);

            }
        });

        String[] mstr = {"00", "15", "30", "45"};
        NumberPicker np2 = (NumberPicker) findViewById(R.id.np3);
        np2.setDisplayedValues(mstr);
        np2.setMinValue(0);
        np2.setMaxValue(3);
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                stats.setText(picker.getDisplayedValues().toString() + "\n" + oldVal + "\n" + newVal);

            }
        });


        String[] astr = {"AM", "PM"};
        NumberPicker np3 = (NumberPicker) findViewById(R.id.np4);
        np3.setDisplayedValues(astr);
        np3.setMinValue(0);
        np3.setMaxValue(1);
        np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                stats.setText(picker.getDisplayedValues().toString() + "\n" + oldVal + "\n" + newVal);

            }
        });

        final TextView te = (TextView) findViewById(R.id.textView);

        final TextView tva = (TextView) findViewById(R.id.tva);
        tva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                te.setText(counter + "");
            }
        });


        TextView tvm = (TextView) findViewById(R.id.tvm);
        tvm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                te.setText(counter + "");
            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                stats.setText("Login Sucess" + "\n" + loginResult.getAccessToken() + "\n" + loginResult.toString());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                stats.setText(object.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                stats.setText("login failed");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeControls() {
        callbackManager = CallbackManager.Factory.create();
        stats = (TextView) findViewById(R.id.status);
        loginButton = (LoginButton) findViewById(R.id.login_button);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
