package services.Interface;

import java.util.Optional;

import controllers.dto.PageFilter;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import play.mvc.Http.Context;

public interface Generic<T> {

    Integer count() throws Exception;

    PagedList<T> getAllData(PageFilter filter) throws Exception;

    Optional<T> getDataByKey(Long id, Integer curdOpt) throws Exception;

    Optional<T> getDataByKeyAndExcludeKey(Long id, Long eId) throws Exception;

    boolean saveOrEdit(T object, Integer curdOpt, Context ctx) throws Exception;

    boolean delete(Long rcdKey) throws Exception;


}
