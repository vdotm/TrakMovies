package com.rainbow.trakmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbow.trakmovies.model.DataModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Meera on 13/10/15.
 */
public class CustomListAdapter extends BaseAdapter {

    private Context context = null;
    //List of data from json
    List<DataModel> dataModelList = null;
    Picasso mPicasso = null;

    public CustomListAdapter(Context context, List<DataModel> dataModelList) {
        super();
        this.context = context;
        mPicasso = Picasso.with(context);
        this.dataModelList = dataModelList;
    }


    @Override
    public int getCount() {
        return dataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customlistview, null, true);
            viewHolder = new ViewHolder();
            viewHolder.textView1 = (TextView) view.findViewById(R.id.movieName);
            viewHolder.textView2 = (TextView) view.findViewById(R.id.movieDetails);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.movieImg);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final DataModel data = dataModelList.get(position);
        String movieDetails = (position + 1) + ". " + data.movie.title + " (" + data.movie.year + ")";
        String ratingTxt =  context.getResources().getString(R.string.rating)
                + " " + data.movie.getRating() + "/10 "
                + " " + context.getResources().getString(R.string.votes)
                + " " + data.movie.votes;

        viewHolder.textView1.setText(movieDetails);
        viewHolder.textView2.setText(ratingTxt);
        //load poster image

        mPicasso.load(data.movie.images.poster.thumb).tag("trak")
                .error(R.drawable.defaultimg)
                .placeholder(R.drawable.defaultimg)
                .into(viewHolder.imageView);

        return view;
    }

    static class ViewHolder {
        TextView textView1;
        TextView textView2;
        ImageView imageView;
    }

}
