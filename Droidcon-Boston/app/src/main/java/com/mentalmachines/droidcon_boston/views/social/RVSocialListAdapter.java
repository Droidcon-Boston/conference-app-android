package com.mentalmachines.droidcon_boston.views.social;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.modal.SocialModal;
import java.util.ArrayList;

class RVSocialListAdapter extends Adapter<RVSocialListAdapter.ListViewHolder> {

  public class ListViewHolder extends ViewHolder {

    ImageView imageView;

    TextView txtView;

    public ListViewHolder(final View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.social_item_img);
      txtView = itemView.findViewById(R.id.social_item_tv);
    }
  }

  ArrayList<SocialModal> socialList;


  public RVSocialListAdapter(final ArrayList<SocialModal> socialList) {
    this.socialList = socialList;
  }

  @Override
  public int getItemCount() {
    return socialList.size();
  }

  @Override
  public void onBindViewHolder(final ListViewHolder holder, final int position) {
    SocialModal socialModal = socialList.get(position);

    holder.txtView.setText(socialModal.getName());

    holder.imageView.setImageResource(socialModal.getImage_resid());

  }

  @Override
  public ListViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
    Context context = parent.getContext();
    View view = LayoutInflater.from(context).inflate(R.layout.social_list_item, parent, false);
    return new ListViewHolder(view);
  }
}
