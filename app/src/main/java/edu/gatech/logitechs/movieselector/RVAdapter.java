package edu.gatech.logitechs.movieselector;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView movieTitle;
        TextView movieYear;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            movieTitle = (TextView)itemView.findViewById(R.id.person_name);
            movieYear = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    List<Movie> movies;

    RVAdapter(List<Movie> movies){
        this.movies = movies;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
//        PersonViewHolder pvh = new PersonViewHolder(v);
//        return pvh;
        return null;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
//        personViewHolder.personName.setText(movies.get(i).name);
//        personViewHolder.personAge.setText(movies.get(i).age);
//        personViewHolder.personPhoto.setImageResource(movies.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}