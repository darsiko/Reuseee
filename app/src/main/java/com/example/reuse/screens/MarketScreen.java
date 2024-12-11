package com.example.reuse.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.reuse.R;


public class MarketScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_screen, container, false);
        // Inflate the layout for this fragment
        // Initialize WebView
        WebView webView = view.findViewById(R.id.webview);  // Replace with your WebView ID
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
// Load the HTML file from the assets folder
        webView.loadUrl("file:///android_asset/openlayers_map.html");

        return view;
    }
}