package com.example.artwokmabel.RubbishReference;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.models.GroupModel;
import com.example.artwokmabel.R;

import java.util.ArrayList;

public class groupsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_groups_fragment, container, false);

        recyclerView = view.findViewById(R.id.groups_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        groupAdapter = new GroupAdapter(this.getContext(), getMyList());
        recyclerView.setAdapter(groupAdapter);

        return view;
    }

    private ArrayList<GroupModel> getMyList() {
        ArrayList<GroupModel> groupModels = new ArrayList<>();

        GroupModel m = new GroupModel();
        m.setGroupname("Chocolate Cake Factories");
        m.setImage(R.drawable.chocolate_cake);
        groupModels.add(m);

        m = new GroupModel();
        m.setGroupname("Jewelier");
        m.setImage(R.drawable.handmade_jewelry);
        groupModels.add(m);

        m = new GroupModel();
        m.setGroupname("Animal Kingdom");
        m.setImage(R.drawable.tiger);
        groupModels.add(m);

        return groupModels;

    }

    class GroupHolder extends RecyclerView.ViewHolder {

        ImageView mimageView;
        TextView mgroupName;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);

            this.mimageView = itemView.findViewById(R.id.group_imageView);
            this.mgroupName = itemView.findViewById(R.id.group_name);
        }
    }

    class GroupAdapter extends RecyclerView.Adapter<GroupHolder> {

        Context context;
        ArrayList<GroupModel> groupModels;

        public GroupAdapter(Context context, ArrayList<GroupModel> groupModels) {
            this.context = context;
            this.groupModels = groupModels;
        }

        @NonNull
        @Override
        public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groups, null);

            return new GroupHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupHolder holder, int position) {

            holder.mgroupName.setText(groupModels.get(position).getGroupname());
            holder.mimageView.setImageResource(groupModels.get(position).getImage());

        }

        @Override
        public int getItemCount() {
            return groupModels.size();
        }
    }
}
