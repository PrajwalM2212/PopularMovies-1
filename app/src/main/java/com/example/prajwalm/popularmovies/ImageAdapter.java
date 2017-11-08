package com.example.prajwalm.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;




 class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{




    private final Context mContext;
    private final ArrayList<JsonResults> mJsonResultsList;
   final private ClickListenerInterface listenerInterface;



     ImageAdapter(Context context , ArrayList<JsonResults> jsonResultsList,ClickListenerInterface listenerInterface){

          this.mContext =context;
          this.mJsonResultsList = jsonResultsList;
          this.listenerInterface =listenerInterface;


    }


     interface ClickListenerInterface{
        void onItemClick(int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        JsonResults jsonResults = mJsonResultsList.get(position);


        ImageView image = holder.imageView;
        String imageUrl = jsonResults.bitmapUrl;

        Uri.Builder builder = Uri.parse("http://image.tmdb.org/t/p/w185"+imageUrl).buildUpon();
        Uri uri =builder.build();
       // TextView textView = holder.textView;
        //textView.setText(uri.toString());



       Picasso.with(mContext).load(uri).into(image);


    }

    @Override
    public int getItemCount() {
        return mJsonResultsList.size();
    }



      class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // --Commented out by Inspection (06/08/17, 9:17 AM):TextView textView;
        final ImageView imageView;
        // --Commented out by Inspection (06/08/17, 9:17 AM):int id;

         ViewHolder(View itemView) {
            super(itemView);
            //textView=(TextView)itemView.findViewById(R.id.text_view);
            imageView= itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {


            int position = getAdapterPosition();
            listenerInterface.onItemClick(position);





        }
    }
}
