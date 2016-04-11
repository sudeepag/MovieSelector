package edu.gatech.logitechs.movieselector.View;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.gatech.logitechs.movieselector.Model.Movie;
import edu.gatech.logitechs.movieselector.R;

public class RVMainAdapter extends RecyclerView.Adapter<RVMainAdapter.MovieViewHolder> {
    /**
     * movies to be displayed
     */
    private List<Movie> movies;
    /**
     * multiplier for the width
     */
    private static final int WIDTH_MULTIPLIER = 4;

    /**
     * constructor
     * @param newMovies the set of movies to display
     */
    RVMainAdapter(List<Movie> newMovies){
        this.movies = newMovies;
    }

    /**
    * Updates the list of movies after fetching it remotely
    *
    * @param newMovies  The new list of movies returned remotely
     **/
    public void updateMovieList(List<Movie> newMovies) {
        movies = newMovies;
        this.notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.muvee_card_view_row, viewGroup, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.movieTitle.setText(movies.get(i).getTitle());
        movieViewHolder.movieDescription.setText(movies.get(i).getDescription());
        if (movies.get(i).getThumbnail() != null) {
            //Scale the Movie Thumbnail
            movieViewHolder.movieImage.setImageBitmap(Bitmap.createScaledBitmap(movies.get(i).getThumbnail(),
                    movies.get(i).getThumbnail().getWidth() * WIDTH_MULTIPLIER,
                    movies.get(i).getThumbnail().getHeight() * WIDTH_MULTIPLIER,
                    false));
        } else {
            movieViewHolder.movieImage.setImageBitmap(null);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        /**
         * title of a movie
         */
        private TextView movieTitle;
        /**
         * description of a movie
         */
        private TextView movieDescription;
        /**
         * image of a movie
         */
        private ImageView movieImage;

        /**
        * Constructor for the MovieViewHolder
        *
        * @param itemView   the view being added
         */
        public MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = (TextView)itemView.findViewById(R.id.movie_title);
            movieDescription = (TextView)itemView.findViewById(R.id.movie_description);
            movieImage = (ImageView)itemView.findViewById(R.id.movie_image_holder);
        }
    }
}