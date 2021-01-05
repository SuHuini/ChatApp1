package i.app.chatapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import android.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Button btn;
    TextInputEditText uname, uemail, pass;
    String TAG = "Authnetication";

    FirebaseDatabase rootNode;
    DatabaseReference databaseClient;
   // private FirebaseAuth mAuth;
    FirebaseFirestore db= FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn = findViewById(R.id.register);
        uname = findViewById(R.id.username);
        uemail = findViewById(R.id.email);
        pass = findViewById(R.id.pass);

        mAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }
    private void register(){

        final String name = uname.getText().toString();
        final String email = uemail.getText().toString();
        final String password = pass.getText().toString();


        //check if all fields are filled
        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Fill in all fields", Toast.LENGTH_LONG).show();
        } //check if email address is accurate
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegisterActivity.this, "Valid email required", Toast.LENGTH_LONG).show();
        }
        else if (password.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Password should have more than 6 digits", Toast.LENGTH_LONG).show();
        }
        else {
            //register user
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                                DocumentReference df = db.collection("chatUsers").document(user.getUid());
                                Map<String, Object> userInfo = new HashMap<>();

                                userInfo.put("name", name);
                                userInfo.put("email", email);
                                userInfo.put("password", password);

                                //userInfo.put("IsUser", "1");

                                df.set(userInfo);

                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                                Log.d(TAG, "saveuserdata");
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }
                        }

                        // ...

                    });
        }
    }

}
