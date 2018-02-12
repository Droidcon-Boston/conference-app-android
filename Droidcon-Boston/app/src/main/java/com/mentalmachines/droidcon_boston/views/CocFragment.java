package com.mentalmachines.droidcon_boston.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.utils.AppConstants;

public class CocFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    View view = inflater.inflate(R.layout.coc_fragment, container, false);

    WebView webView = view.findViewById(R.id.coc_webview);

    // TODO: Add if internet available then load into webview via url, else via local html file
    webView.loadUrl(AppConstants.PATH_TO_ASSETS_FOLDER + "app/index.html");

    return view;
  }


}
