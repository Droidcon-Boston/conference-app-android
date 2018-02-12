package com.mentalmachines.droidcon_boston.views.social;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.modal.SocialModal;
import com.mentalmachines.droidcon_boston.utils.AppConstants;
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
        (view1, position) -> executeLink(socialList.get(position).getLink())));

    return view;
  }

  private void executeLink(String link) {
    Uri data = Uri.parse(link);
    final Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(data);
    startActivity(intent);
  }

  private ArrayList<SocialModal> prepareSocialList() {
    ArrayList<SocialModal> socialList = new ArrayList<>();
    socialList.add(new SocialModal(R.drawable.facebook, "Facebook", AppConstants.SOCIAL_LINK_FB));
    socialList.add(new SocialModal(R.drawable.instagram, "Instagram", AppConstants.SOCIAL_LINK_INSTA));
    socialList.add(new SocialModal(R.drawable.linkedin, "Linkedin", AppConstants.SOCIAL_LINK_LINKD));
    socialList.add(new SocialModal(R.drawable.twitter, "Twitter", AppConstants.SOCIAL_LINK_TWEET));
    return socialList;

  }

}
