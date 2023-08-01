package com.example.demo.entity;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

public class PaginationVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分页数据
     */
    private List<T> list;

    /**
     * 数据的总条数
     */
    private long total;

    /**
     * 每页数据条数
     */
    private int pageSize;

    /**
     * 当前页码
     */
    private int pageNum;

    public PaginationVO() {
    }


    public PaginationVO(List<T> list, long total, int pageSize, int pageNum) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public static <T> PaginationVO<T> build(PageInfo<T> pageInfo) {
        if (null == pageInfo) {
            return new PaginationVO<T>();
        }
        return new PaginationVO<T>(pageInfo.getList(), pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum());
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }


    public static final PaginationVO emptyPage(){
        return new PaginationVO();
    }
}
