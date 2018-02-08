package com.nokia.ices.apps.fusion.score.domain;

import java.util.List;

public class TreeNode {
	
	private String id;
	
	private String text;
	
	private boolean checked;
	
	private boolean hasChildren;
	
	private boolean expanded;
	
	private String spriteCssClass;
	
	private List<TreeNode> items;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public String getSpriteCssClass() {
		return spriteCssClass;
	}

	public void setSpriteCssClass(String spriteCssClass) {
		this.spriteCssClass = spriteCssClass;
	}

	public List<TreeNode> getItems() {
		return items;
	}

	public void setItems(List<TreeNode> items) {
		this.items = items;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

}
