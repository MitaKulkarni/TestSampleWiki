package com.example.samplewiki.Constants;

/**
 * Created by Mita on 8/19/2017.
 */

public class RequestURLs {
    public static final String WIKI_QUERY_URL = "https://en.wikipedia.org//w/api.php?action=query&format=json&prop=pageimages|pageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpssearch=";
    public static final String WIKI_PAGE_URL = "https://en.wikipedia.org/w/api.php?action=query&prop=info&pageids=%s&inprop=url&format=json";
}
