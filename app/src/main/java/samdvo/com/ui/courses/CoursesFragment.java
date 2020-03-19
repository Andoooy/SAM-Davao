package samdvo.com.ui.courses;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Map;

import samdvo.com.Courses;
import samdvo.com.R;

public class CoursesFragment extends Fragment {

    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    DatabaseReference reference;
    Courses courses;
    String name, description;
    int formsuccess;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_courses, null, false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,arrayList);

        listView = v.findViewById(R.id.lvCourses);
        listView.setAdapter(arrayAdapter);
        courses = new Courses();
        reference = FirebaseDatabase.getInstance().getReference().child("Courses");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, String> account = (Map<String, String>) dataSnapshot.getValue();
                String courseName = account.get("courseName");
                arrayList.add(courseName);
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
        inflater.inflate(R.menu.add_course, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnAddCourse:
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Add Course");
                dialog.setContentView(R.layout.dialog_add_course);
                dialog.show();

                final EditText etCourseName = dialog.findViewById(R.id.etCourseName);
                final EditText etCourseDescription = dialog.findViewById(R.id.etCourseDescription);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);
                TextView tvCancel = dialog.findViewById(R.id.tvCancel);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btnAdd:

                                formsuccess = 2;
                                name = etCourseName.getText().toString();
                                description = etCourseDescription.getText().toString();

                                if (name.equals("")) {
                                    etCourseName.setError("This field is required");

                                    formsuccess--;
                                }

                                if (description.equals("")) {
                                    etCourseDescription.setError("This field is required");

                                    formsuccess--;
                                }

                                if (formsuccess==2){
                                    courses.setCourseName(etCourseName.getText().toString().trim());
                                    courses.setCourseDescription(etCourseDescription.getText().toString().trim());
                                    reference.push().setValue(courses);
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success!")
                                            .setContentText("Course Successfully Added!")
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
}
