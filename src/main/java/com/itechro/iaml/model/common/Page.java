package com.itechro.iaml.model.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author : chamara
 */
public class Page<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 3617869045664714881L;

	private long totalRecords = 0;

	private int startIndex = 0;

	private int noOfRecords = 0;

	private int pageLength = 0;

	private Collection<T> page = new ArrayList<T>();

	public Page() {
	}

	public Page(long totalRecords, int startIndex, int noOfRecords, Collection<T> page) {
		this.totalRecords = totalRecords;
		this.startIndex = startIndex;
		this.noOfRecords = noOfRecords;
		this.page = page;
	}

	public Page(long totalRecords, int startIndex, int noOfRecords, int pageLength, Collection<T> page) {
		this.totalRecords = totalRecords;
		this.startIndex = startIndex;
		this.noOfRecords = noOfRecords;
		this.pageLength = pageLength;
		this.page = page;
	}

	public int getNoOfRecords() {
		return noOfRecords;
	}

	public void setNoOfRecords(int endPosition) {
		this.noOfRecords = endPosition;
	}

	public Collection<T> getPageData() {
		return page;
	}

	public void setPageData(Collection<T> pageData) {
		this.page = pageData;
	}

	public int getStartPosition() {
		return startIndex;
	}

	public long getTotalNoOfRecords() {
		return totalRecords;
	}

	public void setTotalNoOfRecords(long totalNoOfRecords) {
		this.totalRecords = totalNoOfRecords;
	}

	public int getPageLength() {
		return pageLength;
	}

	public void setPageLength(int pageLength) {
		this.pageLength = pageLength;
	}

	public int getTotalNoOfPages() {
		int totalNoOfPages = 1;
		if (pageLength != 0) {
			totalNoOfPages = (int) totalRecords / pageLength;
			if (totalRecords % pageLength > 0)
				totalNoOfPages++;
		}
		return totalNoOfPages;
	}

	public int getCurrentPageNo() {
		int currentPageNo = 1;
		if (pageLength != 0) {
			currentPageNo = startIndex / pageLength + 1;
		}
		return currentPageNo;
	}
}
