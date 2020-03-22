// Copyright 2017 Archos SA
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * 
 */
package com.archos.mediacenter.video.utils.oauth;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * A full screen OAuth dialog which contains a webview. This takes an authorize url
 * and returns a filled OAuthData in the OAuthCallback.onFinished method.
 */
public class OAuthDialog extends Dialog {

	private ProgressDialog mProgress;
	private LinearLayout mLayout;
	private WebView mWebView;
	private OAuthCallback mListener;
	private OAuthClientRequest mReq;
	private OAuthData mdata;
	private static final FrameLayout.LayoutParams MATCH = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

	/**
	 * @param context
	 * @param o The OAuth object which calls this dialog
	 * @param url The authorize url
	 */
	public OAuthDialog(Context context, OAuthCallback o,OAuthData oa, OAuthClientRequest req) {
		super(context);
		mdata = oa;
		mReq = req;
		mListener=o;
	}
	
	/**
	 * 
	 * @return The used OAuthData
	 */
	public OAuthData getData() {
		return mdata;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	/**
	 * When the dialog is created, we add the webview and load the authorize url.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mProgress = new ProgressDialog(getContext());
		mProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mProgress.setMessage("Loading...");
		
		mLayout = new LinearLayout(getContext());
		mLayout.setOrientation(LinearLayout.VERTICAL);

		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		//mWebView.getSettings().setSupportZoom(false);
		mWebView.setLayoutParams(MATCH);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.setWebViewClient(new OAuthWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        
        mWebView.loadUrl(mReq.getLocationUri());
        mLayout.addView(mWebView);
        
        Display display = getWindow().getWindowManager().getDefaultDisplay();
		addContentView(mLayout, new FrameLayout.LayoutParams(display.getWidth() - 20, display.getHeight() - 20));
		CookieSyncManager.createInstance(getContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		
	}

	public WebView getWebView(){
		return mWebView;
	}
	
	/**
	 * Set the callback when the authorization ends.
	 * 
	 * @param callback
	 */
	public void setOAuthCallback(OAuthCallback callback) {
		mListener = callback;
	}


	private class OAuthWebViewClient extends WebViewClient {

		/*
        **  Manage if the url should be load or not, and get the result of the request
        **
        */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            Log.d("pref", url);
           	String urldecode = null;
        	try {
				urldecode = URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				
			}
			Uri uri=Uri.parse(urldecode);
			if (!"localhost".equals(uri.getHost())||! urldecode.contains("code="))
        		return false;
        	mdata.code=uri.getQueryParameter("code");
			OAuthDialog.this.dismiss();
	     	mListener.onFinished(mdata);
            
	     	return true;
        }


        /*
        **  Catch the error if an error occurs
        ** 
        */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {
        	super.onReceivedError(view, errorCode, description, failingUrl);
        	if(mListener!=null)
        		mListener.onFinished(mdata);
            OAuthDialog.this.dismiss();
        }


        /*
        **  Display a dialog when the page start
        **
        */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
        	super.onPageStarted(view, url, favicon);
           	mProgress.show();
        }


		/*
		**  Remove the dialog when the page finish loading
		**
		*/
		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			mProgress.dismiss();
			injectCSS();
		}
	}

	//workaround to be accepted on amazon store
	private void injectCSS() {
		try {
			String css = ".col-xs-4 a:focus .btn{ background-color:blue !important; }";
			getWebView().loadUrl("javascript:(function() {" +
					"var parent = document.getElementsByTagName('head').item(0);" +
					"var style = document.createElement('style');" +
					"style.type = 'text/css';" +
					"style.innerHTML=\"" +css+"\";"+
					"parent.appendChild(style);" +
					"})()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
