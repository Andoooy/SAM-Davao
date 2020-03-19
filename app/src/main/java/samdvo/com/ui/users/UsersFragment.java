package samdvo.com.ui.users;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Map;

import samdvo.com.Accounts;
import samdvo.com.R;

public class UsersFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    ListView listView;
    ArrayList <String> arrayList = new ArrayList<>();
    DatabaseReference reference;
    String fullname, username, password;
    Accounts accounts;
    int formsuccess;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_users, null, false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,arrayList);

        listView = v.findViewById(R.id.lvUsers);
        listView.setAdapter(arrayAdapter);
        accounts = new Accounts();
        reference = FirebaseDatabase.getInstance().getReference().child("Accounts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, String> account = (Map<String, String>) dataSnapshot.getValue();
                String fullname = account.get("fullname");
                arrayList.add(fullname);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnAddUser:
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Add User");
                dialog.setContentView(R.layout.dialog_add_user);
                dialog.show();

                final EditText etFullname = dialog.findViewById(R.id.etFullname);
                final EditText etUsername = dialog.findViewById(R.id.etUsername);
                final EditText etPassword = dialog.findViewById(R.id.etPassword);
                final Spinner spinnerUserType = dialog.findViewById(R.id.spinnerUserType);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.user_type, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUserType.setAdapter(adapter);
                spinnerUserType.setOnItemSelectedListener(this);
                Button btnRegister = dialog.findViewById(R.id.btnRegister);
                TextView tvCancel = dialog.findViewById(R.id.tvCancel);

                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btnRegister:

                                formsuccess = 3;
                                fullname = etFullname.getText().toString();
                                username = etUsername.getText().toString();
                                password = etPassword.getText().toString();

                                if (fullname.equals("")) {
                                    etFullname.setError("This field is required");

                                    formsuccess--;
                                }

                                if (username.equals("")) {
                                    etUsername.setError("This field is required");

                                    formsuccess--;
                                }

                                if (password.equals("")) {
                                    etPassword.setError("This field is required");

                                    formsuccess--;
                                }

                                else if (password.length() < 8) {
                                    etPassword.setError("Password must be at least 8 characters");

                                    formsuccess--;
                                }

                                if (formsuccess == 3) {
                                    accounts.setUsername(etUsername.getText().toString().trim());
                                    accounts.setPassword(etPassword.getText().toString().trim());
                                    accounts.setFullname(etFullname.getText().toString().trim());
                                    accounts.setUsertype(spinnerUserType.getSelectedItem().toString());
                                    reference.push().setValue(accounts);
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success!")
                                            .setContentText("Account Successfully Registered!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }

                                break;
                        }
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
