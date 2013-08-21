package com.navbuilder.app.hurricane.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.navbuilder.app.hurricane.BaseActivity;
import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.controller.CategoryController;
import com.navbuilder.app.hurricane.customview.ExpandableColumnLayout;
import com.navbuilder.app.hurricane.customview.ExpandableColumnLayout.OnItemClickListener;
import com.navbuilder.app.hurricane.customview.ListViewAdapter;
import com.navbuilder.app.hurricane.model.Category;

public class AddCategoryActivity extends BaseActivity implements OnItemClickListener {

	private ExpandableColumnLayout mBrandsLayout;

	private ExpandableColumnLayout mCategoryLayout;

	public static final String SELECTED_CATEGORY = "selected_category";
	public static final String CATEGORY_TYPE = "category_type";
	public static final String CATEGORY_CODE = "category_code";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_category_view);
		overridePendingTransition(R.anim.slid_in_from_bottom, R.anim.slid_out_from_top);

		SearchView searchView = (SearchView)this.findViewById(R.id.search_category_view);
		searchView.setFocusable(false);
		searchView.setIconified(false);
		searchView.setQueryHint("Search for brands or categories to add");
		int searchPlateId = searchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) searchView.findViewById(searchPlateId);
		textView.setTextColor(Color.GRAY);
		textView.setHintTextColor(Color.WHITE);
		textView.setTextSize(14);
		
		getActionBar().setTitle("Add Interest");
		getActionBar().setIcon(R.drawable.actionbar_icon);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		initCategories();
		mBrandsLayout = findViewByIdAutoCast(R.id.brands_gridview);
		mCategoryLayout = findViewByIdAutoCast(R.id.categories_gridview);

		initInterestsLayout(mBrandsLayout, Category.TYPE_BRAND);
		initInterestsLayout(mCategoryLayout, Category.TYPE_INTEREST);
	}

	private void initInterestsLayout(ExpandableColumnLayout layout, int type) {
		int orientation = getResources().getConfiguration().orientation;
		layout.setColumnNum(orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
		layout.setHorizontalMargin(10);
		// layout.setVerticalMargin(10);
		layout.setAdapter(getAdapter(type));
		layout.setCollapseState(6);
		layout.setOnItemClickListener(this);
		layout.setExpanderLayout(R.layout.category_expander, R.id.expand, R.id.collapse);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private ListViewAdapter getAdapter(int type) {
		ListViewAdapter adapter = new ListViewAdapter(this, R.layout.category_item, R.id.category_item_text);
		switch (type) {
		case Category.TYPE_BRAND:
			for (Category c : brands) {
				adapter.add(c);
			}
			break;
		case Category.TYPE_INTEREST:
			for (Category c : interests) {
				adapter.add(c);
			}
			break;
		}
		return adapter;
	}

	private List<Category> interests;
	private List<Category> brands;

	private void initCategories() {
		interests = new ArrayList<Category>();
		brands = new ArrayList<Category>();
		for (Category c : CategoryController.getInstance().getAllCategories()) {
			switch (c.getType()) {
			case Category.TYPE_BRAND:
				brands.add(c);
				break;
			case Category.TYPE_INTEREST:
				interests.add(c);
				break;
			}
		}
	}

	@Override
	public void onItemClick(ExpandableColumnLayout parent, View view, int position, long id) {
		Category c = (Category) parent.getAdapter().getItem(position);
		Intent intent = this.getIntent();
		intent.putExtra(SELECTED_CATEGORY, c.getName());
		intent.putExtra(CATEGORY_TYPE, c.getType());
		switch (parent.getId()) {
		case R.id.categories_gridview:
			intent.putExtra(CATEGORY_CODE, c.getCode());
			break;
		}
		setResult(Activity.RESULT_OK, intent);
		overridePendingTransition(R.anim.slid_in_from_bottom, R.anim.slid_out_from_top);
		finish();
	}

}
