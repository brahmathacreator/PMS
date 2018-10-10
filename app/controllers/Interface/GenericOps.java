package controllers.Interface;

import play.mvc.Result;

public interface GenericOps {

    Result index(Integer curdOpt, String activeMenu);

    Result search(Integer curdOpt, String activeMenu, Integer currentPage, String searchBy, String searchValue, String currentSortBy, String currentOrder);

    Result selectRecord(Integer curdOpt, String activeMenu, Long id);

    Result curdOperations(Integer curdOpt, String activeMenu);


}
