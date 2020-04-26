package com.example.whatsappclone;

import android.app.Application;

import com.parse.Parse;

public class WhatsappParse extends Application {
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("pFLDbjvezwHHjjvvJpRFPdUTFy9ZBXVVxJze7mhQ")
                .clientKey("XVM3xnTs4Bl2t72cW6qvKHn0dwfTINvOGtDIcWfH")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
