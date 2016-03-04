package edu.gatech.logitechs.movieselector;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MovieViewHolder> {

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView movieTitle;
        TextView movieDescription;
        ImageView movieImage;

        public MovieViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            movieTitle = (TextView)itemView.findViewById(R.id.movie_title);
            movieDescription = (TextView)itemView.findViewById(R.id.movie_description);
            movieImage = (ImageView)itemView.findViewById(R.id.movie_image_holder);
        }
    }

    List<Movie> movies;

    RVAdapter(List<Movie> movies){
        this.movies = movies;
    }

    public void updateMovieList(List<Movie> newMovies) {
        movies = newMovies;
        this.notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.muvee_card_view_row, viewGroup, false);
        MovieViewHolder pvh = new MovieViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder MovieViewHolder, int i) {
        MovieViewHolder.movieTitle.setText(movies.get(i).getTitle());
        MovieViewHolder.movieDescription.setText(movies.get(i).getDescription());
        if (movies.get(i).getThumbnail() != null) {
            //Scale the Movie Thumbnail
            MovieViewHolder.movieImage.setImageBitmap(Bitmap.createScaledBitmap(movies.get(i).getThumbnail(),
                    (int) (movies.get(i).getThumbnail().getWidth() * 4),
                    (int) (movies.get(i).getThumbnail().getHeight() * 4),
                    false));
        } else {
            MovieViewHolder.movieImage.setImageBitmap(null);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}