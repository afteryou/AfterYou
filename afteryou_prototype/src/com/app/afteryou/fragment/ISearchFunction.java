package com.app.afteryou.fragment;

public interface ISearchFunction {

	public void doSearch(int type, SearchParams params);

	public class SearchParams {
		public String query = "";
		public String[] categories = null;
		public String[] brands = null;
	}
}
