package app.nthnvrml.com.newsbeautifier;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.nthnvrml.com.newsbeautifier.RSS.model.RssItem;

/**
 * Created by vermel on 12/03/16.
 */
public class RSSListAdapter extends RecyclerView.Adapter<RSSListAdapter.ViewHolder> {
    private ArrayList<RssItem> rssItems;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView pubDate;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.firstLine);
            pubDate = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    // Constructor
    public RSSListAdapter(ArrayList<RssItem> rssItems) {
        this.rssItems = rssItems;
    }

    // Add item and notify it
    public void add(int position, RssItem item) {
        this.rssItems.add(position, item);
        notifyItemInserted(position);
    }
    // Remove item and notify it
    public void remove(RssItem item) {
        int position = this.rssItems.indexOf(item);
        this.rssItems.remove(position);
        notifyItemRemoved(position);
    }

    //Return le viewHolder avec la vue qui lui correspont. i
    // ci le cas est simple nous n'avons qu'un seul Type de vue;
    @Override
    public RSSListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }




    // On ajoute les valeurs dans les views
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.title.setText(this.rssItems.get(position).getTitle());
        holder.pubDate.setText(this.rssItems.get(position).getPubDate().toString());
    }

    // retourne le nombre d'item dans l'adapter. (item a afficher)
    @Override
    public int getItemCount() {
        return this.rssItems.size();
    }

}