package app.nthnvrml.com.newsbeautifier.detail;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import app.nthnvrml.com.newsbeautifier.MainActivity;
import app.nthnvrml.com.newsbeautifier.R;
import app.nthnvrml.com.newsbeautifier.RSS.model.RssItem;

/**
 * Created by vermel on 12/03/16.
 */
public class DetailList extends Fragment {

    public static final String TAG_DETAIL_LIST = "TAG_DETAIL_LIST";
    RssItem currentItem;
    private ProgressDialog progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressBar = new ProgressDialog(getContext());
        return inflater.inflate(R.layout.detail_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentItem = getArguments().getParcelable(getContext().getResources().getString(R.string.extra_rssitems));

        assert currentItem != null;
        if (currentItem.getTitle() != null)  {
            ((TextView) view.findViewById(R.id.title_)).setText(currentItem.getTitle());
            if (((MainActivity) getContext()).getSupportActionBar() != null)
                ((MainActivity) getContext()).getSupportActionBar().setTitle(currentItem.getTitle());
        }
        if (currentItem.getLink() != null) {
            TextView linkTextView = ((TextView) view.findViewById(R.id.link));
            linkTextView.setText(currentItem.getLink());
            linkTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

            WebView webView = (WebView) view.findViewById(R.id.webview);
            webView.setWebViewClient(new WebViewClient() {

                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if (progressBar != null && progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                    progressBar = ProgressDialog.show(getContext(), "News Beautiful", "Loading...");
                }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    if (progressBar != null && progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            });


            webView.loadUrl(currentItem.getLink());
        }
    }





}
