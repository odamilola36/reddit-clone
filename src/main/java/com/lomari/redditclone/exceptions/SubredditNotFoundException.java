package com.lomari.redditclone.exceptions;

public class SubredditNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SubredditNotFoundException(String str) {
        super(str);
    }

}
