package edu.gatech.logitechs.movieselector.View;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import edu.gatech.logitechs.movieselector.Controller.UserManager;
import edu.gatech.logitechs.movieselector.Model.User;
import edu.gatech.logitechs.movieselector.R;

public class RVAdminAdapter extends RecyclerView.Adapter<RVAdminAdapter.UserViewHolder> {

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        CardView cvAdmin;
        TextView userTitle;

        Switch banSwitch;
        Switch lockSwitch;

        /*
        * Constructor for the UserViewHolder
        *
        * @param itemView   the view being added
         */
        public UserViewHolder(View itemView) {
            super(itemView);
            cvAdmin = (CardView)itemView.findViewById(R.id.cv_admin);
            userTitle = (TextView)itemView.findViewById(R.id.admin_user);

            banSwitch = (Switch) itemView.findViewById(R.id.admin_ban_switch);
            banSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        banSwitch.setText("Banned");
                        UserManager.banUser(((User) banSwitch.getTag()).getUID());
                    } else {
                        banSwitch.setText("Unbanned");
                        UserManager.unbanUser(((User) banSwitch.getTag()).getUID());
                    }
                }
            });
            if (banSwitch.isChecked()) {
                banSwitch.setText("Banned");
            } else {
                banSwitch.setText("Unbanned");
            }

            lockSwitch = (Switch) itemView.findViewById(R.id.admin_lock_switch);
            lockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        lockSwitch.setText("Locked");
                        UserManager.lockUser(((User) banSwitch.getTag()).getUID());
                    } else {
                        lockSwitch.setText("Unlocked");
                        UserManager.unlockUser(((User) banSwitch.getTag()).getUID());

                    }
                }
            });

            if (lockSwitch.isChecked()) {
                lockSwitch.setText("Locked");
            } else {
                lockSwitch.setText("Unlocked");
            }

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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.muvee_admin_card_view_row, viewGroup, false);
        UserViewHolder pvh = new UserViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {

        userViewHolder.banSwitch.setTag(users.get(i));
        userViewHolder.lockSwitch.setTag(users.get(i));

        userViewHolder.userTitle.setText(users.get(i).getEmail());
        userViewHolder.banSwitch.setChecked(users.get(i).isBanned());
        userViewHolder.lockSwitch.setChecked(users.get(i).isLocked());


    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}