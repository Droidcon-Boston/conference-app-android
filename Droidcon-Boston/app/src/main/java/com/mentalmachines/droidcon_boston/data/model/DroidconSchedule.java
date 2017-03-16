package com.mentalmachines.droidcon_boston.data.model;

import java.util.List;

/**
 * Created by jinn on 3/15/17.
 */

public class DroidconSchedule {

    public Integer id;
    public String date;
    public String dateGmt;
    public Guid guid;
    public String modified;
    public String modifiedGmt;
    public String slug;
    public String status;
    public String type;
    public String link;
    public Title title;
    public Content content;
    public Excerpt excerpt;
    public Integer author;
    public Integer featuredMedia;
    public Integer parent;
    public Integer menuOrder;
    public String commentStatus;
    public String pingStatus;
    public String template;
    public List<Object> meta = null;
    public Acf acf;
    public Links links;

}