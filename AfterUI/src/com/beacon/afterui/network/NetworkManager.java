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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NetworkManager {

	/** TAG */
	private static final String TAG = NetworkManager.class.getSimpleName();

	private static boolean DEBUG = true;

	private static final String BASE_URL = "https://76.74.223.195/obweb/AYWS/";

	public static final String SIGN_UP_REQUEST_URL = "saveUser.php";

	public static final String SIGN_IN_REQUEST_URL = "loginUser.php";

	public static final String ACTIVATE_USER = "updateUserStatus.php";

	public static final String UPLOAD_IMAGE = "uploadUserImage.php";

	public static AsyncHttpClient syncClient = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			final RequestListener listener) {
		syncClient.get(getAbsoluteUrl(url), params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						if (listener != null) {
							listener.onSuccess(response);
						}
					}

					@Override
					public void onFailure(Throwable thr, JSONObject failed) {
						if (listener != null) {
							listener.onFailure(getErrorCode(failed));
						}
					}

				});
	}

	public static void post(String url, RequestParams params,
			final RequestListener listener) {
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory
				.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		syncClient.setSSLSocketFactory(socketFactory);
		syncClient.post(getAbsoluteUrl(url), params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						listener.onSuccess(response);
					}

					@Override
					public void onFailure(Throwable thr, JSONObject failed) {
						listener.onFailure(getErrorCode(failed));
					}

				});
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void uploadImage(final Map<String, String> data,
			final RequestListener listener, final Handler handler) {
		if (DEBUG) {
			Log.d(TAG, "ImageUpload()");
		}

		if (data == null) {
			Log.d(TAG, "uploadImage: data is null , please check");
			return;
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
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));

				HttpParams params = new BasicHttpParams();
				params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
						HttpVersion.HTTP_1_1);
				params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
				params.setParameter(
						ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
						new ConnPerRouteBean(30));
				params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE,
						false);

				DefaultHttpClient client = new DefaultHttpClient(
						new SingleClientConnManager(params, registry), params);
				HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

				HttpPost dataPost = new HttpPost(UPLOAD_IMAGE);

				List<NameValuePair> dataToPost = new ArrayList<NameValuePair>();

				Set<String> keySet = data.keySet();
				for (String key : keySet) {
					dataToPost.add(new BasicNameValuePair(key, data.get(key)));

				}

				try {
					dataPost.setEntity(new UrlEncodedFormEntity(dataToPost));

					// Execute HTTP Post Request
					HttpResponse response = client.execute(dataPost);
					HttpEntity entity = response.getEntity();

					InputStream ins = entity.getContent();
					final JSONObject json = readData(ins);
					Log.d(TAG, "SUCCESS : " + json.toString());

					if (json != null && json.has(ParsingConstants.ID)) {
						if (handler != null) {
							handler.post(new Runnable() {

								@Override
								public void run() {

									if (listener != null) {
										listener.onSuccess(json);
									}

								}
							});
						} else {
							if (listener != null) {
								listener.onSuccess(json);
							}

						}
					} else {
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

				catch (IOException ex) {
					if (listener != null) {
						listener.onFailure(NetworkConstants.REQUEST_FAILED);
					}
				}
			}
		}).start();
	}

	public static void signUp(final Map<String, String> data,
			final RequestListener listener, final Handler handler) {
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
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));

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
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs));
						HttpResponse httpResponse = httpClient
								.execute(httppost);
						HttpEntity entityTemp = httpResponse.getEntity();

						InputStream insTemp = entityTemp.getContent();
						final JSONObject jsonTemp = readData(insTemp);
						Log.d(TAG, "Status Update : " + jsonTemp);
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
										listener.onSuccess(json);
									}
								}
							});
						} else {
							if (listener != null) {
								listener.onSuccess(json);
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
		try {
			if (json.getString(NetworkConstants.ERROR_CODE).equalsIgnoreCase(
					NetworkConstants.EMAIL_ALREADY_TAKEN_ERROR)) {
				errorCode = NetworkConstants.SignUpRequestConstants.EMAIL_ALREADY_TAKEN;
			}
		} catch (JSONException ex) {

		}
		return errorCode;
	}

	public interface RequestListener {
		public void onSuccess(final JSONObject json);

		public void onFailure(final int errorCode);
	}

	public static void signIn(final Map<String, String> data,
			final RequestListener listener, final Handler handler) {
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
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));

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
									listener.onSuccess(json);
								}
							}
						});
					} else {
						if (listener != null) {
							listener.onSuccess(json);
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
