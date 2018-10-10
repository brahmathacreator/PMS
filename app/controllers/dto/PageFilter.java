package controllers.dto;

import java.util.HashMap;
import java.util.Map;

public class PageFilter {

    private Integer currentPage;
    private String searchColumn;
    private String searchValue;
    private String orderByColumn;
    private String orderByValue;

    private Map<Long, String> schools = new HashMap<Long, String>();
    private Map<Long, String> batches = new HashMap<Long, String>();
    private Map<Long, String> sections = new HashMap<Long, String>();

    public PageFilter() {
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(String searchColumn) {
        this.searchColumn = searchColumn;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getOrderByValue() {
        return orderByValue;
    }

    public void setOrderByValue(String orderByValue) {
        this.orderByValue = orderByValue;
    }

    public Map<Long, String> getSchools() {
        return schools;
    }

    public void setSchools(Map<Long, String> schools) {
        this.schools = schools;
    }

    public Map<Long, String> getBatches() {
        return batches;
    }

    public void setBatches(Map<Long, String> batches) {
        this.batches = batches;
    }

    public Map<Long, String> getSections() {
        return sections;
    }

    public void setSections(Map<Long, String> sections) {
        this.sections = sections;
    }
}
