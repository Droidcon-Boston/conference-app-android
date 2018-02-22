package com.mentalmachines.droidcon_boston.views.social;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.modal.SocialModal;
import com.mentalmachines.droidcon_boston.utils.CustomTabUtil;
import com.mentalmachines.droidcon_boston.utils.DividerItemDecoration;
import com.mentalmachines.droidcon_boston.utils.RVItemClickListener;
import java.util.ArrayList;

public class SocialFragment extends Fragment {

  ArrayList<SocialModal> socialList;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.social_fragment, container, false);

    RecyclerView rv = view.findViewById(R.id.social_rv);

    // Set Layout Manager
    rv.setLayoutManager(new LinearLayoutManager(getActivity()));

    // Set the divider
    rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

    socialList = prepareSocialList();
    // Set Adapter
    rv.setAdapter(new RVSocialListAdapter(socialList));

    // Set On Click
    rv.addOnItemTouchListener(new RVItemClickListener(getActivity(),
        (view1, position) -> CustomTabUtil.loadUriInCustomTab(getContext(), socialList.get(position).getLink())));

    return view;
  }

  private ArrayList<SocialModal> prepareSocialList() {
    ArrayList<SocialModal> socialList = new ArrayList<>();
    socialList.add(new SocialModal(R.drawable.social_facebook, getString(R.string.social_title_facebook),
        getString(R.string.facebook_link)));
    socialList.add(new SocialModal(R.drawable.social_instagram, getString(R.string.social_title_instagram),
        getString(R.string.instagram_link)));
    socialList.add(new SocialModal(R.drawable.social_linkedin, getString(R.string.social_title_linkedin),
        getString(R.string.linkedin_link)));
    socialList.add(new SocialModal(R.drawable.social_twitter, getString(R.string.social_title_twitter),
        getString(R.string.twitter_link)));
    return socialList;

  }

}
