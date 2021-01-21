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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import i.app.chatapp1.Model.User;

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
                //writeNewUser();
               // nextActivity();
            }
        });


    }
    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    private void register(){

        final String name = uname.getText().toString();
        final String email = uemail.getText().toString();
        final String userPass = pass.getText().toString();

        final String password =  sha256(userPass);
        Log.d(TAG, "Hashed password" + password);

           // String passw = pass.getText().toString();
           // String newPass =  sha256(passw);

        //check if all fields are filled
        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Fill in all fields", Toast.LENGTH_LONG).show();
        } //check if email address is accurate
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegisterActivity.this, "Valid email required", Toast.LENGTH_LONG).show();
        }
        else if (userPass.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Password should have more than 6 digits", Toast.LENGTH_LONG).show();
        }
        else {
            //register user
            mAuth.createUserWithEmailAndPassword(email, userPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();
                                databaseClient = FirebaseDatabase.getInstance().getReference("ChatUsers");
                                //updateUI(user);
                                Log.d(TAG,  "RB user added");
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userId);
                                hashMap.put("name", name);
                                hashMap.put("email", email);
                                hashMap.put("password", userPass);
                                hashMap.put("imageUrl", "default");
                                databaseClient.push().setValue(hashMap);
                                Log.d(TAG,  "writenewuser user added");
                                writeNewUser(name, email, userPass);
                                //nextActivity();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }
    private void writeNewUser(String name, String email, String password) {
        //final String userId;
//        final String name = uname.getText().toString();
//        final String email = uemail.getText().toString();
//        final String password = pass.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        //DocumentReference df = db.collection("chatUsers").document(user.getUid());
        String userId = user.getUid();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", userId);
        userInfo.put("name", name);
        userInfo.put("email", email);
        userInfo.put("password", password);
        userInfo.put("imageUrl", "default");

        //userInfo.put("IsUser", "1");
        db.collection("chatUsers")
                .add(userInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID" + documentReference.getId());
                        nextActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document");
                    }
                });



        Log.d(TAG, "saveuserdata");

    }
    private void nextActivity(){
        Intent i = new Intent(RegisterActivity.this, StartActivity.class);
        startActivity(i);
        finish();
        Log.d(TAG, "writenewuser save user data");
    }


}
