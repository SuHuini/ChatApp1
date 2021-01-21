package i.app.chatapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    Button btn;
    TextInputEditText uemail, pass;
    private FirebaseAuth mAuth;
    String TAG = "Authnetication";

    FirebaseDatabase rootNode;
    DatabaseReference databaseSignIn;
    //private FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        uemail = findViewById(R.id.em);
        pass = findViewById(R.id.passl);
        btn = findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }
    private void login() {
        mAuth.signInWithEmailAndPassword(uemail.getText().toString(), pass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                    @Override
                    public void onSuccess(AuthResult authresult) {
                        Log.d(TAG, "signInWithEmail:success");
                        //AuthResult authResult;
                        FirebaseUser user = mAuth.getInstance().getCurrentUser();
                        checkUserAccessLevel(authresult.getUser().getUid());

                        Toast.makeText(LoginActivity.this, "Authentication successful.",
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }
    public void checkUserAccessLevel(String uid) {
        DocumentReference databaseClient = firestore.collection("chatUsers").document(uid);
        databaseClient.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "signInWithEmail:success");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        });
    }
    private void signIn(){
        String email = uemail.getText().toString();
        String password = pass.getText().toString();

        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)){
                Toast.makeText(LoginActivity.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                                //FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }                        }
                    });
        }
    }



}
//
//        firestore.collection("chatUsers").whereEqualTo( "email", uemail.getText().toString()).whereEqualTo( "password", pass.getText().toString())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//@Override
//public void onComplete(@NonNull Task<QuerySnapshot> task) {
//        if(task.isSuccessful()){
//        for(QueryDocumentSnapshot doc: task.getResult()){
//        String email = uemail.getText().toString();
//        String password = pass.getText().toString();
//        String em = doc.getString("email");
//        String pwd = doc.getString("password");
//        Log.d(TAG, "signInWithEmail:success" + doc.getData());
//        Toast.makeText(LoginActivity.this, "Authentication successful.",
//        Toast.LENGTH_SHORT).show();
//
////                                if(em == email
////                                        && pwd == password){
////                                    Log.d(TAG, "signInWithEmail:success" + doc.getData());
////                                    Toast.makeText(LoginActivity.this, "Authentication successful.",
////                                            Toast.LENGTH_SHORT).show();
////
////                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
////                                    startActivity(i);
////                                    finish();
////                                } else{
////                                     //If sign in fails, display a message to the user.
////                            Log.w(TAG, "signInWithEmail:failure", task.getException());
////                            Toast.makeText(LoginActivity.this, "Authentication failed.",
////                                    Toast.LENGTH_SHORT).show();
////                            //updateUI(null);
////                                }
//        }
//        }
//        else{
//        //If sign in fails, display a message to the user.
//        Log.w(TAG, "signInWithEmail:failure", task.getException());
//        Toast.makeText(LoginActivity.this, "TASK FAILED.",
//        Toast.LENGTH_SHORT).show();
//        //updateUI(null)
//        }
//        }
//        });

//final String uId = uid;

//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            //updateUI(user);
//                            DocumentReference databaseClient = firestore.collection("chatUsers").document(uId);
//                            databaseClient.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    Log.d(TAG, "signInWithEmail:success" + documentSnapshot.getData());
//                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(i);
//                                    finish();
//                                }
//                            });
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });

//        mAuth.signInWithEmailAndPassword(uemail.getText().toString(), pass.getText().toString())
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//
//                    @Override
//                    public void onSuccess(AuthResult authresult) {
//                        Log.d(TAG, "signInWithEmail:success");
//                        //AuthResult authResult;
//                        FirebaseUser user = mAuth.getInstance().getCurrentUser();
//                     //String uId = authresult.getUser().getUid();
//                        DocumentReference databaseClient = firestore.collection("chatUsers").document(authresult.getUser().getUid());
//
//
//                                Log.d(TAG, "signInWithEmail:success" + documentSnapshot.getData());
//                                Toast.makeText(LoginActivity.this, "Authentication successful.",
//                                        Toast.LENGTH_SHORT).show();
//
//                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                                startActivity(i);
//                                finish();
//                            }
//
//                                    });
//
//                mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        AuthResult authResult;
//                        if (task.isSuccessful(AuthResult auth)) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            //updateUI(user);
//
//                            AuthResult authResult;
//                            //String uid = authResult.getUser().getUid();
//                            DocumentReference databaseClient = firestore.collection("chatUsers").document(authResult.getUser().getUid());
//                            databaseClient.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    Log.d(TAG, "signInWithEmail:success" + documentSnapshot.getData());
//                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(i);
//                                    finish();
//                                }
//                            });
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });