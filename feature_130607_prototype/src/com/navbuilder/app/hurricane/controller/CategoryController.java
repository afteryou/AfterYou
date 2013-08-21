package com.navbuilder.app.hurricane.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.navbuilder.app.hurricane.application.HurricaneApplication;
import com.navbuilder.app.hurricane.constants.CategoryConstants;
import com.navbuilder.app.hurricane.customview.ListItem;
import com.navbuilder.app.hurricane.model.Category;
import com.navbuilder.app.hurricane.utils.AppUtils;
import com.navbuilder.app.hurricane.utils.FileUtils;

public class CategoryController {

	private static CategoryController instance;
	private Context mContext;
	private SharedPreferences mCategoryPreferences;
	private final static List<Category> EMPTY_LIST = new ArrayList<Category>(0);

	private CategoryController() {
		mContext = HurricaneApplication.getInstance().getApplicationContext();
	}

	public synchronized static CategoryController getInstance() {
		if (instance == null) {
			instance = new CategoryController();
		}
		return instance;
	}

	private String getJsonDataFromAsset(String assetName) {
		InputStream is = null;
		InputStreamReader reader = null;
		try {
			is = mContext.getAssets().open(assetName);
			reader = new InputStreamReader(is);
			char[] data = new char[is.available()];
			reader.read(data);
			return String.valueOf(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			FileUtils.close(is);
			FileUtils.close(reader);
		}
	}

	private List<Category> getCategoriesFromJsonData(String jsonString) {
		JSONObject json;
		try {
			json = new JSONObject(jsonString);
			JSONArray interests = json.getJSONArray(CategoryConstants.CATEGORY_TYPE_INTEREST);
			List<Category> result = new ArrayList<Category>();
			for (int i = 0; i < interests.length(); i++) {
				JSONObject object = interests.getJSONObject(i);
				Category c = new Category(mContext.getApplicationContext());
				c.setName(object.getString(CategoryConstants.CATEGROY_NAME));
				c.setCode(object.getString(CategoryConstants.CATEGROY_CODE));
				c.setIcon(object.getString(CategoryConstants.CATEGORY_ICON));
				c.setType(Category.TYPE_INTEREST);
				result.add(c);
			}
			if (!json.has(CategoryConstants.CATEGORY_TYPE_BRANDS)) {
				return result;
			}
			JSONArray brands = json.getJSONArray(CategoryConstants.CATEGORY_TYPE_BRANDS);
			for (int i = 0; i < brands.length(); i++) {
				JSONObject object = brands.getJSONObject(i);
				Category c = new Category(mContext.getApplicationContext());
				c.setName(object.getString(CategoryConstants.CATEGROY_NAME));
				c.setType(Category.TYPE_BRAND);
				result.add(c);
			}
			return result;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Category> getAllCategories() {
		return getCategoriesFromJsonData(getJsonDataFromAsset("all_categories_brands.json"));
	}

	public List<Category> getUserAttachedCategoriesFromPref() {
		String userAttached = initCategoryPreference().getString(CategoryConstants.PREF_KEY_USER_ATTACHED_CATEGORIES,
				null);
		if (userAttached == null) {
			if (AppUtils.isFirstTimeStart()) {
				return getUserCategoriesFromPref();
			}
			return getEmptyList();
		}

		return getCategoriesFromJsonData(userAttached);
	}

	public List<Category> getUserCategoriesFromPref() {
		initCategoryPreference();
		String mUserCategories = mCategoryPreferences.getString(CategoryConstants.PREF_KEY_USER_CATEGORIES,
				CategoryConstants.EMPTY);
		if (mUserCategories.equals(CategoryConstants.EMPTY)) {
			mUserCategories = getJsonDataFromAsset("default_categories_brands.json");
			Editor editor = mCategoryPreferences.edit();
			editor.putString(CategoryConstants.PREF_KEY_USER_CATEGORIES, mUserCategories);
			editor.commit();
		}
		return getCategoriesFromJsonData(mUserCategories);
	}

	public void saveUserCategoriesToPref(List<ListItem> categories) {
		saveCategoriesToPref(CategoryConstants.PREF_KEY_USER_CATEGORIES, categories);
	}

	public void saveUserAttachedCategoriesToPref(List<Category> attached) {
		saveCategoriesToPref(CategoryConstants.PREF_KEY_USER_ATTACHED_CATEGORIES, attached);
	}

	private void saveCategoriesToPref(final String preferenceKey, final List<? extends ListItem> categories) {
		new Thread(new Runnable() {
			public void run() {
				JSONArray interests = new JSONArray();
				JSONArray brands = new JSONArray();

				try {
					for (ListItem item : categories) {
						if (item instanceof Category) {
							Category category = (Category) item;
							JSONObject jo = new JSONObject();
							jo.put(CategoryConstants.CATEGROY_NAME, category.getName());
							jo.put(CategoryConstants.CATEGROY_CODE, category.getCode());
							jo.put(CategoryConstants.CATEGORY_ICON, category.getIcon());
							jo.put(CategoryConstants.CATEGORY_TYPE, category.getType());
							switch (category.getType()) {
							case Category.TYPE_BRAND:
								brands.put(jo);
								break;
							case Category.TYPE_INTEREST:
								interests.put(jo);
								break;
							}
						}
					}

					Editor editor = mCategoryPreferences.edit();
					JSONObject jo = new JSONObject();
					jo.putOpt(CategoryConstants.CATEGORY_TYPE_BRANDS, brands);
					jo.putOpt(CategoryConstants.CATEGORY_TYPE_INTEREST, interests);
					editor.putString(preferenceKey, jo.toString());
					editor.commit();
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}

	private SharedPreferences initCategoryPreference() {
		if (mCategoryPreferences == null) {
			mCategoryPreferences = mContext.getSharedPreferences(CategoryConstants.CATEGROY_PREFERENCE,
					Context.MODE_PRIVATE);
		}
		return mCategoryPreferences;
	}

	private List<Category> getEmptyList() {
		EMPTY_LIST.clear();
		return EMPTY_LIST;
	}
}
