package edu.gatech.logitechs.movieselector;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdminAdapter extends RecyclerView.Adapter<RVAdminAdapter.UserViewHolder> {

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        CardView cvAdmin;
        TextView userTitle;

        /*
        * Constructor for the UserViewHolder
        *
        * @param itemView   the view being added
         */
        public UserViewHolder(View itemView) {
            super(itemView);
            cvAdmin = (CardView)itemView.findViewById(R.id.cv_admin);
            userTitle = (TextView)itemView.findViewById(R.id.admin_user);
        }
    }

    List<User> users;

    RVAdminAdapter(List<User> users){
        this.users = users;
    }

    /*
    * Updates the list of users after fetching it remotely
    *
    * @param newUsers  The new list of users returned remotely
    *
     */
    public void updateUserList(List<User> newUsers) {
        users = newUsers;
        this.notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.muvee_card_view_row, viewGroup, false);
        UserViewHolder pvh = new UserViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {
        userViewHolder.userTitle.setText(users.get(i).getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}