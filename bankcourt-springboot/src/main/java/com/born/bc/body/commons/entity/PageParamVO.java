package com.born.bc.body.commons.entity;

/**
 * 分页参数VO
 */
public class PageParamVO {

    /**
     * 当前页码
     */
    private Integer page;
    /**
     * 每页显示条数
     */
    private Integer limit;
    /**
     * 总条数
     */
    private long count;

    public Integer getPage() {
        return (page == null) ? 1 : page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return (limit == null) ? 10 : limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "PageParamVO [page=" + page + ", limit=" + limit + ", count=" + count + "]";
	}




}
