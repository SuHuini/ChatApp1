package i.app.chatapp1.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import i.app.chatapp1.Adapters.UserAdapter;
import i.app.chatapp1.Model.User;
import i.app.chatapp1.R;



public class UsersFragment extends Fragment {

    String TAG = "Users";

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private CollectionReference fStore;
    DocumentReference doc ;
    FirebaseAuth mAuth;
    String name;
    //TextView uemail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mUsers = new ArrayList<>();
        readUsers();
        return view;

    }


    public void readUsers(){
        //final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseUser user = mAuth.getCurrentUser();
       fStore = FirebaseFirestore.getInstance().collection("chatUsers");
//        doc = fStore.document(user.getUid());
//        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                name = documentSnapshot.getString("name");
//                Log.d(TAG, "Name Retrieved" + name);
//            }
//        });

        fStore.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mUsers.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                User user = document.toObject(User.class);
                                mUsers.add(user);
                                //assert user != null;
                                //assert firebaseUser != null;
                                //String name = user.getName();
//                                if(user.getId().equals(firebaseUser.getUid())){
//                                    mUsers.add(user);
//                                }
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                userAdapter = new UserAdapter(getContext(), mUsers);
                                recyclerView.setAdapter(userAdapter);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }


}
