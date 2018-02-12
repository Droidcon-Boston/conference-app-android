package com.mentalmachines.droidcon_boston.modal;

public class SocialModal {

  private int image_resid;

  private String link;

  private String name;

  public SocialModal(final int image_resid, final String name, final String link) {
    this.image_resid = image_resid;
    this.name = name;
    this.link = link;
  }

  public int getImage_resid() {
    return image_resid;
  }

  public void setImage_resid(final int image_resid) {
    this.image_resid = image_resid;
  }

  public String getLink() {
    return link;
  }

  public void setLink(final String link) {
    this.link = link;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}
