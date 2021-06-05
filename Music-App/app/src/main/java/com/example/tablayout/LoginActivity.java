package com.example.tablayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tablayout.ui.main.UserInfor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText txtUserName,txtPassword,txtEmail;
    Button btnSignIn;
    private FirebaseAuth mAuth;
    public static final String IDACCOUNT = "IDACCOUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        txtUserName = findViewById(R.id.edUserName);
        txtPassword = findViewById(R.id.edPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    private void login() {
        final String UserName = txtUserName.getText().toString().trim();
        String Password = txtPassword.getText().toString().trim();

        if(UserName.isEmpty()){
            txtUserName.setError("Vui Lòng Nhập Email");
            txtUserName.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(UserName).matches()){
            txtUserName.setError("Vui Lòng Nhập Email Hợp Lệ");
            txtUserName.requestFocus();
        }
        else if(Password.isEmpty()){
            txtPassword.setError("Vui Lòng Nhập Mật Khẩu");
            txtPassword.requestFocus();
        }
        else if (Password.length() < 6){
            txtPassword.setError("Độ Dài Mật Khẩu Tối Thiểu Là 6 Ký Tự");
        }
        else if (UserName.isEmpty() && Password.isEmpty()){
            Toast.makeText(LoginActivity.this, "Các Ô Không Được Để Trống", Toast.LENGTH_SHORT).show();
        }
        // khi nhập đủ thông tin
        else if(!(UserName.isEmpty() && Password.isEmpty())){
            mAuth.signInWithEmailAndPassword(UserName,Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Đăng Nhập Lỗi, Vui Lòng Thử Lại", Toast.LENGTH_SHORT).show();
                    }
                    // thành công
                    else {
                        updateUI();
                    }
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this, "Có Lỗi Xảy Ra!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        saveData(mAuth.getCurrentUser().getUid()) ;
        UserInfor userInfor = UserInfor.getInstance();
        userInfor.setID(mAuth.getCurrentUser().getUid());
        Log.d("demo", (mAuth.getCurrentUser().getUid())) ;
        Intent HomePage = new Intent(LoginActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
        startActivity(HomePage);
    }

    private void saveData(String userID) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IDACCOUNT, userID);
        editor.apply();
    }
}