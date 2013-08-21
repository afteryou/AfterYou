/*
 * (C) Copyright 2012 by TeleCommunication Systems, Inc.
 *
 * The information contained herein is confidential, proprietary
 * to TeleCommunication Systems, Inc., and considered a trade secret
 * as defined in section 499C of the penal code of the State of
 * California. Use of this information by anyone other than
 * authorized employees of TeleCommunication Systems is granted only
 * under a written non-disclosure agreement, expressly prescribing
 * the scope and manner of such use.
 *
 */

package com.app.afteryou.preference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;

import com.app.afteryou.log.Nimlog;
import com.app.afteryou.R;
/**
 * 
 * @author lxie
 * Contains a Tree Data Structure, but only used in file name separate rules.
 */
public class GPSFileExplorer extends ViewFlipper {
    ////////////////////////Leon Begins////////////////////
    public class FileNode{
          private String sName;
          
          public FileNode father;
          public ArrayList<FileNode> children;
          
          /**
           * get the depth of this tree node in tree
           * @return
           */
          public int getDepth(){
        	  int depth = 0;
        	  FileNode node = this;
        	  for(;;){
        		  if(node.father != null){
        			  depth++;
        			  node = node.father;
        		  }
        		  else{
        			  break;
        		  }
        	  }
        	  return depth;
          }
          
          /**
           * If the parameter node is a child node of this
           * @param node
           * @return
           */
          public boolean hasChild(FileNode node){
                boolean result = false;
                if(this.children == null){
                	return false;
                }
                for(FileNode fNode : children){
                      if(fNode.equals(node)){
                            result = true;
                      }
                }
                return result;
          }
          
          public boolean isFolder(){
        	  boolean result = false;
        	  if(this.hasChildren()){
        		  result = true;
        	  }
        	  if(this.sName.indexOf(File.separator) >= 0){
        		  result = true;
        	  }
        	  return result;
          }
          
          /**
           * If current node has any children
           * @return
           */
          public boolean hasChildren(){
        	  if(this.children == null || this.children.size() == 0){
        		  return false;
        	  }
        	  return true;
          }
          
          /**
           * get the full name, including full directory
           * @return
           */
          public String getFullName(){
        	  String s = this.sName;
        	  FileNode node = this;
        	  for(;;){
        		  if(node.father == null){
        			  break;
        		  }
        		  if(node.father.getName() == null){
        			  break;        			  
        		  }
        		  if(node.father.getName().trim().equals("")){
        			  break;
        		  }
        		  s = node.father.getName() + s;
        		  node = node.father;        		  
        	  }
        	  return s;
          }
          
          public String getName(){
                return this.sName;
          }
          
          public void setName(String s){
                this.sName = s;
          }
    }
    
    FileNode root = new FileNode();
    
    public int getFileTreeDepth(ArrayList<String> array){
    	int depth = 0, pos = -1, tempDepth = 0;
    	for(String fileName : array){
    		tempDepth = 1;
    		for(int index = 0;index < fileName.length();index++){
    			if(Character.toString(fileName.charAt(index)).equals(File.separator)){
    				tempDepth++;
	    		}	    		
    		}
    		if(tempDepth > depth){
    			depth = tempDepth;
    		}
    	}
    	return depth;
    }
    
    /**
     * Tree Generating Rule
     * !!!!!!!!!!!!!!PAY ATTENTION TO THE SEQUENCE!!!!!!!!!!!
     * @return
     */
    public ArrayList<String> getSeparateRules(){
    	ArrayList<String> rules = new ArrayList<String>();
    	rules.add("/sdcard/gps/");
    	/**!!!!!!!!!!!This should be put at Last Because it was contain
    	 * in the rules above!!!!!!!!!!!!!!!!!!!
    	 */
    	rules.add(File.separator);
    	return rules;
    }
    
    /**
     * initialize the WHOLE tree, to use this function, pass the root node and the File Name arraylist into it.
     * it will generate the whole tree automatically.
     * @param father
     * @param array
     */
    public int initFileTree(FileNode father, /*String dir, */ArrayList<String> array, int deep){
    	  //FileNode node = new FileNode();
    	int depth = deep;
    	String tempDir = null, fileName;
    	int pos;
    	for(int i = 0;i < array.size();i++){
    		depth = deep + 1;
    		fileName = array.get(i);
    		if(father.children == null){
    			father.children = new ArrayList<FileNode>();
    		}
    		boolean matchRule = false;
    		ArrayList<String> rules = getSeparateRules();
    		for(String rule : rules)
    		{
	    		if((pos = fileName.indexOf(rule)) >= 0){
	    			tempDir = fileName.substring(0, pos + rule.length());
	    			matchRule = true;
	    			break;
	    		}
    		}
    		if(!matchRule){
    			FileNode node = new FileNode();
    			node.setName(fileName);
    			node.father = father;
    			father.children.add(node);
    		}
    		if(tempDir == null){
    			continue;
    		}
    		boolean exists = false;
    		int index = 0;
    		for(;index < father.children.size();index ++){
    			if(tempDir.trim().equals(father.children.get(index).getName().trim())){
    				exists = true;
    				break;
    			}
    		}
    		FileNode nodeRes;
    		if(exists){
     //                 nodeRes = father.children.get(index);
    		}else{
    			nodeRes = new FileNode();
    			nodeRes.setName(tempDir);
    			nodeRes.father = father;
    			father.children.add(0, nodeRes);                   
    			ArrayList<String> subArray = filtArray(array, tempDir);
    			int tempDepth = initFileTree(nodeRes, /*tempDir, */subArray, depth);
    			if(tempDepth > depth){
    				depth = tempDepth;
    			}
    		}
    	}
    	return depth;
    }
    
    /**
     * a util function used by tree initializing, cut of and filter the file name array list to do the next depth level
     * @param array
     * @param pattern
     * @return
     */
    public ArrayList<String> filtArray(ArrayList<String> array, String pattern){
          ArrayList<String> result = new ArrayList<String>();
          String temp;
          for(String name : array){
                if(name.trim().length() <= pattern.trim().length()){
                      continue;
                }
                temp = name.substring(0, pattern.length()).trim();
                if(temp.equals(pattern)){
                      temp = name.substring(pattern.length(), name.length());
                      result.add(temp);
                }
          }
          return result;
    }
    //////////////////////////Leon Ends///////////////////////
	
    ///////////////////////Bruce Begins///////////////////////
	/**
	 * This method is used to collect all files under the folder and its
	 * sub-folders
	 * 
	 * @param folder
	 *            : the folder name under which GPS files are searched.
	 * @param files
	 *            : the list of GPS path; * The GPS files which located under
	 *            folder will be added into this list
	 * */
	private void getGPSFileNameInAsset(String folder, ArrayList<String> files){
		AssetManager assetManager = context.getAssets();
		String[] fileNames;
		try {
			fileNames = assetManager.list(folder);
		}catch (IOException e) {
			Nimlog.v(this, e.toString());
			return; 
		}
		if(fileNames != null && fileNames.length > 0){
			for(String file : fileNames){
				if(isGPSFile(file)){
					files.add(folder + File.separator + file);
				}else if(isKnownFile(file)){
					continue;
				}else if(isFolder(folder + File.separator + file)){
					getGPSFileNameInAsset(folder + File.separator + file, files);
				}
			}
		}
	}
	
	private boolean isFolder(String file) {
		if(file == null || file.indexOf(".") >= 0){
			return false;
		}
		
		AssetManager assetManager = context.getAssets();
		String[] fileNames = null;
		try {
			fileNames = assetManager.list(file);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return fileNames != null && fileNames.length >0;
	}
	
	private boolean isGPSFile(String file) {
		int index = file.lastIndexOf(".");
		if (index != -1) {
			String tmp = file.substring(index + 1);
			if (tmp != null && tmp.length() > 0) {
				if (tmp.equalsIgnoreCase("gps")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	//this is used to speed up the file process, as I/O operation is time costing
	private boolean isKnownFile(String file){
		if(file.equalsIgnoreCase("n1")){
			return true;
		}
		if(file.equalsIgnoreCase("n2")){
			return true;
		}
		if(file.equalsIgnoreCase("n3")){
			return true;
		}
		if(file.equalsIgnoreCase("sky")){
			return true;
		}
		if(file.equalsIgnoreCase("3DCARTEX")){
			return true;
		}
		if(file.equalsIgnoreCase("NIGHT_SKY")){
			return true;
		}
		if(file.equalsIgnoreCase("AVATAR_3D_32")){
			return true;
		}
		if(file.equalsIgnoreCase("filesets")){
			return true;
		}
		if(file.equalsIgnoreCase("END_FLAG")){
			return true;
		}
		if(file.equalsIgnoreCase("tokens.properties")){
			return true;
		}
		return false;
	}
	private ArrayList<String> getGpsFiles() {
		ArrayList<String> dataGpsList = new ArrayList<String>();
		getGPSFileNameInAsset("data", dataGpsList);
		Collections.sort(dataGpsList);
		
		ArrayList<String> sdcardGpsList = new ArrayList<String>();
		try{
			File gpsFolder = new File("/sdcard/gps");
			if(gpsFolder.isDirectory()){
				File[] files = gpsFolder.listFiles();
				if(files != null){
					for(int i = 0; i < files.length ; i++){
						String name = files[i].getName();
						String tmp = null;
						int index = name.lastIndexOf(".");
						if (index != -1) {
							tmp = name.substring(index + 1);
							if (tmp != null && tmp.length() > 0) {
								if (tmp.equalsIgnoreCase("gps")) {
									sdcardGpsList.add("/sdcard/gps/" + name);
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			Nimlog.i(this, "Read external gps files error.");
		}
		Collections.sort(sdcardGpsList);
		
		for(int i=0; i<sdcardGpsList.size(); ++i){
			dataGpsList.add(sdcardGpsList.get(i));
		}
		return dataGpsList;
	}
	////////////////////////Bruce Ends/////////////////////////
	Context context;
	private Animation slideInLeft;
	private Animation slideInRight;
	private Animation slideOutLeft;
	private Animation slideOutRight;
	private LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT,
			LayoutParams.FILL_PARENT);
	private String selectedName;
	
	private int treeDepth = 0;
	
	public interface GPSFileListener{
		public void onGpsFileSelected(String fullFileName);
	}	
	private GPSFileListener mListener;
	public void setGPSFileListener(GPSFileListener listener) {
		this.mListener = listener;
	}
	
	public GPSFileExplorer(Context context, String defaultName) {
		super(context);
		this.context = context;
		selectedName = defaultName;
		ArrayList<String> array = getGpsFiles();
		array.add(0, context.getString(R.string.REAL_GPS));
//		array.add(1, "Standalone GPS (non-LK)");
		initFileTree(root, array, 0);
		treeDepth = getFileTreeDepth(array) + 1;//
		Nimlog.d(this, "Tree Depth: " + treeDepth);
		
		for(int i = 0;i < treeDepth;i++){
			ListView listView = new ListView(context);
			listView.setCacheColorHint(android.R.color.transparent);
			listView.setVerticalScrollBarEnabled(false);
			mListViewArray.add(listView);
			
			m_FileNodeArray.add(new Hashtable<Integer, FileNode>());
			m_AdapterArray.add(new FileNodeAdapter(m_FileNodeArray.get(i)));
			addView(listView);
		}
		
		for(int i = 0;i < root.children.size();i++){
			m_FileNodeArray.get(0).put(i, root.children.get(i));
		}
		
		FileNodeAdapter adapter = new FileNodeAdapter(m_FileNodeArray.get(0));
		m_AdapterArray.add(0, adapter);		
		mListViewArray.get(0).setAdapter(adapter);
		
		slideInLeft = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_left);
		slideInRight = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_right);
		slideOutRight = AnimationUtils.loadAnimation(context,
				R.anim.slide_out_right);
		slideOutLeft = AnimationUtils.loadAnimation(context,
				R.anim.slide_out_left);
		
		for(int i = 0;i < mListViewArray.size();i++){
			ListView listView = mListViewArray.get(i);
			final int index = i;
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					FileNode node = (FileNode)m_FileNodeArray.get(index).get(position);
					handleListClick(arg1, node, position);
				}
			});
		}
	}
	//ArrayList's size = Tree's Depth
	private ArrayList<ListView> mListViewArray = new ArrayList<ListView>();
	private ArrayList<Hashtable<Integer, FileNode>> m_FileNodeArray = new ArrayList<Hashtable<Integer, FileNode>>();
	private ArrayList<FileNodeAdapter> m_AdapterArray = new ArrayList<FileNodeAdapter>();
		
	public void backToPrev() {
		int moveTo = getDisplayedChild() - 1;
		if (moveTo >= 0) {
			setOutAnimation(slideOutRight);
			setInAnimation(slideInRight);
			setDisplayedChild(moveTo);
		}
	}
	
	public boolean isShowingChildren() {
		return getDisplayedChild() > 0;
	}
	
	private void populateParentData(FileNode node){
		int depth = node.getDepth();
		if(depth == 0){
			Nimlog.i(this, "No Parents node.");
			return;
		}
		else if(depth == 1){
			populateRootData(root);
			return;
		}
		depth--;
		m_FileNodeArray.get(depth).clear();
		for(int i = 0;i < node.father.father.children.size();i++){
			m_FileNodeArray.get(depth).put(i, node.father.father.children.get(i));
		}
		
		m_AdapterArray.remove(depth);
		FileNodeAdapter adapter = new FileNodeAdapter(m_FileNodeArray.get(depth));
		m_AdapterArray.add(depth, adapter);
		
		mListViewArray.get(depth).setAdapter(adapter);
		mListViewArray.get(depth).requestFocus();
	}
	
	private void populateRootData(FileNode root){
		m_FileNodeArray.get(0).clear();
		for(int i = 0;i < root.children.size();i++){
			m_FileNodeArray.get(0).put(i, root.children.get(i));
		}
		if(m_AdapterArray.size() > 0)
			m_AdapterArray.remove(0);
		FileNodeAdapter adapter = new FileNodeAdapter(m_FileNodeArray.get(0));
		m_AdapterArray.add(0, adapter);
		
		mListViewArray.get(0).setAdapter(adapter);
		mListViewArray.get(0).requestFocus();
	}
	
	protected void handleListClick(View view, FileNode node, int position) {
		int depth = node.getDepth();
		mListViewArray.get(depth).requestFocus();
		
		if(!node.hasChildren())//File, can be returned to Activity
		{
			//TODO: pass this to activity.
			selectedName = node.getFullName();
			if(!node.isFolder()){
				mListener.onGpsFileSelected(node.getFullName());
			}
			return;
		}
		//if (mListener != null)
		//	mListener.onCategorySelected(view, c);
		populateChildData(node);
		this.setInAnimation(slideInLeft);
		this.setOutAnimation(slideOutLeft);
		this.setDisplayedChild(depth);		
	}
	
	private void populateChildData(FileNode father){
		int depth = father.getDepth();
		if(!father.hasChildren()){
			Nimlog.i(this, "No Children node.");
			return;
		}
		m_FileNodeArray.get(depth).clear();
		for(int i = 0;i < father.children.size(); i++){
			m_FileNodeArray.get(depth).put(i, father.children.get(i));
		}
		m_AdapterArray.remove(depth);
		FileNodeAdapter adapter = new FileNodeAdapter(m_FileNodeArray.get(depth));
		m_AdapterArray.add(depth, adapter);
		
		mListViewArray.get(depth).setAdapter(adapter);
		mListViewArray.get(depth).requestFocus();
	}
	
	class FileNodeAdapter extends BaseAdapter {
		private Hashtable<Integer, FileNode> mListData;

		public FileNodeAdapter(Hashtable<Integer, FileNode> listData) {
			this.mListData = listData;
		}

		@Override
		public int getCount() {
			return mListData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.find_cate_item, null);
			}
			ImageView more = (ImageView) convertView.findViewById(R.id.fc_more);

			ImageView type = (ImageView) convertView
			.findViewById(R.id.fc_icon);
			
			TextView text = (TextView) convertView.findViewById(R.id.fc_text);
			FileNode node = mListData.get(position);

			String str = node.getName();
			
			if(str.endsWith(File.separator)){
				str = str.substring(0, str.length() - 1);
			}
//			if (position == 0) {
//				text.setText(R.string.IDS_ALL);
//			} else {
				text.setText(str);
//			}

			if(node.isFolder()){
				more.setImageResource(R.drawable.map_bubble_arrow);
				type.setBackgroundResource(R.drawable.ic_menu_archive);
			}
			else{
				if(node.getFullName().trim().equalsIgnoreCase(selectedName.trim())){
					more.setImageResource(R.drawable.check);
				}
				else{
					more.setImageResource(R.drawable.blank);
				}
				type.setBackgroundResource(R.drawable.ic_menu_compose);
			}
			return convertView;
		}
	}
	
}