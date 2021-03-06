package co.domi.clase10.activities;

import androidx.appcompat.app.AppCompatActivity;
import co.domi.clase10.R;
import co.domi.clase10.comm.Actions;
import co.domi.clase10.events.OnUserListListener;
import co.domi.clase10.model.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private User myUser;
    private ListView userList;
    private ArrayAdapter<User> userArrayAdapter;
    private ArrayList<User> users;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        //Configuracion de la lista
        userList = findViewById(R.id.userList);
        users = new ArrayList<>();
        userArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        userList.setAdapter(userArrayAdapter);
        //Recuperar el user de la actividad pasada
        myUser = (User) getIntent().getExtras().getSerializable("myUser");
        //Listar usuarios
        db = FirebaseFirestore.getInstance();

        Query userReference = db.collection("users").orderBy("username").limit(10);
        userReference.get().addOnCompleteListener(
                task -> {
                    users.clear();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        User user = doc.toObject(User.class);
                        users.add(user);
                    }
                    userArrayAdapter.notifyDataSetChanged();
                }
        );



        //Habilitar los clicks a items de la lista
        userList.setOnItemClickListener(this);
    }



    //Se activa cuando damos click a un item de la lista
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User userClicked = users.get(position);
        Intent intent = new Intent(this, ChatActivity.class);

        intent.putExtra("myUser",this.myUser);
        intent.putExtra("userClicked",userClicked);
        
        startActivity(intent);
    }
}