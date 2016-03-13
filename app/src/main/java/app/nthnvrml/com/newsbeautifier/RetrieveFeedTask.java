package app.nthnvrml.com.newsbeautifier;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import app.nthnvrml.com.newsbeautifier.RSS.RssFeed;
import app.nthnvrml.com.newsbeautifier.RSS.RssReader;
import app.nthnvrml.com.newsbeautifier.RSS.model.RssItem;

/**
 * Created by vermel on 12/03/16.
 */

public class RetrieveFeedTask extends AsyncTask<String, Void, ArrayList<RssItem>> {

    Context context;

    public RetrieveFeedTask(Context c) {
        this.context = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<RssItem> doInBackground(String... FeedUrl) {
        RssFeed feed = null;
        try {
            URL url = new URL(FeedUrl[0]);
            feed = RssReader.read(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feed.getRssItems();
    }

    @Override
    protected void onPostExecute(ArrayList<RssItem> rssItems) {
        super.onPostExecute(rssItems);
        for (RssItem rssItem : rssItems) {
            Log.i("RSS Reader title", rssItem.getTitle());
            Log.i("RSS Reader", rssItem.getContent());
            Log.i("RSS Reader", rssItem.getTitle());
        }

        Bundle bundle = new Bundle();
        Fragment fragment = new RSSListFragment();

        bundle.putParcelableArrayList(this.context.getString(R.string.extra_rss), rssItems);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = ((MainActivity) this.context).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                .commit();
    }

}