package com.example.artwokmabel.notifs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.login.LoginOptionsActivity;
import com.example.artwokmabel.models.Notification;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NotifsFragment extends Fragment {

    private View view;
    private ArrayList<Notification> notificationsList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifs, container, false);

        recyclerView = view.findViewById(R.id.notifs_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        final NotifsAdapter notifsAdapter = new NotifsAdapter(getActivity(), notificationsList);
        recyclerView.setAdapter(notifsAdapter);

        Notification testNotif = new Notification(
                "data:image/jpeg;base64,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",
                "Trump"
        );
        notificationsList.add(testNotif);
        notificationsList.add(testNotif);
        notificationsList.add(testNotif);
        notificationsList.add(testNotif);
        notificationsList.add(testNotif);
        notifsAdapter.notifyDataSetChanged();


        view.findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginOptionsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
