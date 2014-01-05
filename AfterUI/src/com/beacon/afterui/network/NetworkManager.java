package com.beacon.afterui.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

public class NetworkManager {

    /** TAG */
    private static final String TAG = NetworkManager.class.getSimpleName();

    private static boolean DEBUG = true;

    private static final String SIGN_UP_REQUEST_URL = "https://76.74.223.195/obweb/AYWS/saveUser.php";

    private static final String SIGN_IN_REQUEST_URL = "https://76.74.223.195/obweb/AYWS/loginUser.php";

    private static final String ACTIVATE_USER = "https://76.74.223.195/obweb/AYWS/updateUserStatus.php";

    public static void signUp(final Map<String, String> data,
            final SignUpRequestListener listener, final Handler handler) {
        if (DEBUG) {
            Log.d(TAG, "signUp()");
        }

        if (data == null) {
            Log.e(TAG, "signUp: data is NULL, can't complete sign up request!");
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory
                        .getSocketFactory();
                socketFactory
                        .setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

                DefaultHttpClient client = new DefaultHttpClient();
                SingleClientConnManager mgr = new SingleClientConnManager(
                        client.getParams(), registry);

                // Create a new HttpClient and Post Header
                HttpParams params = new BasicHttpParams();
                params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                        HttpVersion.HTTP_1_1);
                params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
                params.setParameter(
                        ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
                        new ConnPerRouteBean(30));
                params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE,
                        false);

                DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
                        client.getParams());

                // Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                HttpPost httppost = new HttpPost(SIGN_UP_REQUEST_URL);

                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    Set<String> keySet = data.keySet();
                    for (String key : keySet) {
                        nameValuePairs.add(new BasicNameValuePair(key, data
                                .get(key)));
                    }

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpClient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    InputStream ins = entity.getContent();
                    final JSONObject json = readData(ins);
                    Log.d(TAG, "SUCCESS : " + json.toString());

                    if (json == null || !json.has(ParsingConstants.ID)) {
                    	
                        if (handler != null) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    if (listener != null) {
                                        listener.onFailure(getErrorCode(json));
                                    }
                                }
                            });
                        } else {
                            if (listener != null) {
                                listener.onFailure(getErrorCode(json));
                            }
                        }
                        return;
                    } else {

                        String userId = "-1";
                        try {
                            userId = json.getString(ParsingConstants.ID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Activate the user.
                        nameValuePairs.clear();
                        nameValuePairs.add(new BasicNameValuePair(
                                ParsingConstants.USER_ID, userId));
                        nameValuePairs.add(new BasicNameValuePair(
                                ParsingConstants.STATUS, "active"));
                        httppost = new HttpPost(ACTIVATE_USER);
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse httpResponse = httpClient.execute(httppost);
                        HttpEntity entityTemp = httpResponse.getEntity();

                        InputStream insTemp = entityTemp.getContent();
                        final JSONObject jsonTemp = readData(insTemp);
                        Log.d( TAG, "Status Update : " + jsonTemp);
                        // Execute HTTP Post Request
                        if (jsonTemp == null) {
                            if (handler != null) {
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        if (listener != null) {
                                            listener.onFailure(NetworkConstants.REQUEST_FAILED);
                                        }
                                    }
                                });
                            } else {
                                if (listener != null) {
                                    listener.onFailure(NetworkConstants.REQUEST_FAILED);
                                }
                            }
                            return;
                        }
                        if (handler != null) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    if (listener != null) {
                                        listener.onSignUp(json);
                                    }
                                }
                            });
                        } else {
                            if (listener != null) {
                                listener.onSignUp(json);
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "signUp: " + e.getMessage());
                    if (handler != null) {
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.onFailure(NetworkConstants.REQUEST_FAILED);
                                }
                            }
                        });
                    } else {
                        if (listener != null) {
                            listener.onFailure(NetworkConstants.REQUEST_FAILED);
                        }
                    }
                }
            }
        }).start();
    }

    protected static int getErrorCode(JSONObject json) {
    	int errorCode = NetworkConstants.REQUEST_FAILED;
    	try{
    		if(json.getString(NetworkConstants.ERROR_CODE).equalsIgnoreCase(NetworkConstants.EMAIL_ALREADY_TAKEN_ERROR))
    		{
    			errorCode = NetworkConstants.SignUpRequestConstants.EMAIL_ALREADY_TAKEN;
    		}
    	}
    	catch(JSONException ex)
    	{
    	
    	}
		return errorCode;
	}

	public interface SignUpRequestListener {
        public void onSignUp(final JSONObject json);

        public void onFailure(final int errorCode);
    }

    public interface SignInRequestListener {
        public void onSignIn(final JSONObject json);

        public void onFailure(final int errorCode);
    }

    public static void signIn(final Map<String, String> data,
            final SignInRequestListener listener, final Handler handler) {
        if (DEBUG) {
            Log.d(TAG, "signIn()");
        }

        if (data == null) {
            Log.e(TAG, "signIn: data is NULL, can't complete sign in request!");
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory
                        .getSocketFactory();
                socketFactory
                        .setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

                DefaultHttpClient client = new DefaultHttpClient();
                SingleClientConnManager mgr = new SingleClientConnManager(
                        client.getParams(), registry);

                // Create a new HttpClient and Post Header
                HttpParams params = new BasicHttpParams();
                params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                        HttpVersion.HTTP_1_1);
                params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
                params.setParameter(
                        ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
                        new ConnPerRouteBean(30));
                params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE,
                        false);

                DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
                        client.getParams());

                // Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                HttpPost httppost = new HttpPost(SIGN_IN_REQUEST_URL);

                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    Set<String> keySet = data.keySet();
                    for (String key : keySet) {
                        nameValuePairs.add(new BasicNameValuePair(key, data
                                .get(key)));
                    }

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpClient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    InputStream ins = entity.getContent();
                    final JSONObject json = readData(ins);
                    Log.d(TAG, "signIn SUCCESS : " + json.toString());

                    if (handler != null) {
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.onSignIn(json);
                                }
                            }
                        });
                    } else {
                        if (listener != null) {
                            listener.onSignIn(json);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "signIn: " + e.getMessage());
                    if (handler != null) {
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.onFailure(NetworkConstants.REQUEST_FAILED);
                                }
                            }
                        });
                    } else {
                        if (listener != null) {
                            listener.onFailure(NetworkConstants.REQUEST_FAILED);
                        }
                    }
                }
            }
        }).start();
    }

    private static JSONObject readData(final InputStream inputStream) {
        BufferedReader r = new BufferedReader(
                new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject json = null;
        if (total.length() > 0) {
            try {
                json = new JSONObject(total.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return json;
    }
}