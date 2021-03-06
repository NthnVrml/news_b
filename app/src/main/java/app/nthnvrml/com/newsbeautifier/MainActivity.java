package app.nthnvrml.com.newsbeautifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import app.nthnvrml.com.newsbeautifier.RSS.RssFeed;
import app.nthnvrml.com.newsbeautifier.RSS.RssReader;
import app.nthnvrml.com.newsbeautifier.RSS.model.RssItem;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progressDialog;
    Context context;

    public static final String URL_NEWS = "http://news.yahoo.com/rss/";
    public static final String URL_HEALTH = "https://fr.news.yahoo.com/rss/sante";
    public static final String URL_TOP = "http://www.feedforall.com/sample-feed.xml";
    public static final String URL_SOCCER = "http://sports.yahoo.com/soccer//rss.xml";
    public static final String URL_HORSE_RACING = "https://sports.yahoo.com/box/rss.xml";
    public static final String URL_CYCLING = "https://sports.yahoo.com/sc/rss.xml";
    public static final String URL_BOXING = "https://sports.yahoo.com/box/rss.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle(getResources().getString(R.string.enter_your_feed));
                alertDialog.setMessage("RSS");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.mipmap.ic_launcher);

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (input != null &&
                                        input.getText() != null) {
                                    new RetrieveFeedTask(context).execute(input.getText().toString());
                                }
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }

        });

        new RetrieveFeedTask(this).execute(URL_NEWS);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(this);
        if (id == R.id.nav_all_news) {
            retrieveFeedTask.execute(URL_NEWS);
            getSupportActionBar().setTitle("All News");
        } else if (id == R.id.nav_health) {
            retrieveFeedTask.execute(URL_HEALTH);
            getSupportActionBar().setTitle("Health");
        } else if (id == R.id.nav_soccer) {
            retrieveFeedTask.execute(URL_SOCCER);
            getSupportActionBar().setTitle(getResources().getString(R.string.soccer));
        } else if (id == R.id.nav_ridding) {
            retrieveFeedTask.execute(URL_HORSE_RACING);
            getSupportActionBar().setTitle(getResources().getString(R.string.horse_racing));
        } else if (id == R.id.nav_cycling) {
            retrieveFeedTask.execute(URL_CYCLING);
            getSupportActionBar().setTitle(getResources().getString(R.string.cycling));
        } else if (id == R.id.nav_boxing) {
            retrieveFeedTask.execute(URL_BOXING);
            getSupportActionBar().setTitle(getResources().getString(R.string.boxing));
        } else if (id == R.id.nav_other) {
            retrieveFeedTask.execute(URL_TOP);
            getSupportActionBar().setTitle(getResources().getString(R.string.top));
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_message));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class RetrieveFeedTask extends AsyncTask<String, Void, ArrayList<RssItem>> {

        Context context;
        String error = null;

        public RetrieveFeedTask(Context c) {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = ProgressDialog.show(MainActivity.this, "News Beautiful", "Loading...");
        }

        @Override
        protected ArrayList<RssItem> doInBackground(String... FeedUrl) {
            RssFeed feed = null;
            try {
                URL url = new URL(FeedUrl[0]);
                feed = RssReader.read(url);
            } catch (MalformedURLException e) {
                error = "MalformedURLException";
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (feed != null) {
                return feed.getRssItems();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<RssItem> rssItems) {
            super.onPostExecute(rssItems);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (rssItems != null && !rssItems.isEmpty()) {
//                for (RssItem rssItem : rssItems) {
//                    if (rssItem.getTitle() != null)
//                        Log.i("Title", rssItem.getTitle());
//                    if (rssItem.getContent() != null )
//                        Log.i("Content", rssItem.getContent());
//                    if (rssItem.getDescription() != null)
//                        Log.i("Description", rssItem.getDescription());
//
//                }


                    Bundle bundle = new Bundle();
                    Fragment fragment = new RSSListFragment();

                    bundle.putParcelableArrayList(this.context.getString(R.string.extra_rss), rssItems);
                    fragment.setArguments(bundle);

                    FragmentManager fragmentManager = ((MainActivity) this.context).getSupportFragmentManager();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();


            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                if (error != null && error.equals("MalformedURLException")) {
                    builder.setMessage(getResources().getString(R.string.error_parser));
                } else {
                    builder.setMessage(getResources().getString(R.string.not_connected));
                }
                builder.setTitle(getResources().getString(R.string.warning));
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setCancelable(false);
                builder.setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }


        }

    }
}
