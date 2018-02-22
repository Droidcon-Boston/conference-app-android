package com.mentalmachines.droidcon_boston.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.mentalmachines.droidcon_boston.R;

public class CocFragment extends Fragment {

  public CocFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    View view = inflater.inflate(R.layout.coc_fragment, container, false);

    WebView webView = view.findViewById(R.id.coc_webview);

    webView.loadUrl(getResources().getString(R.string.path_to_coc_html_file));

    return view;
  }


}
