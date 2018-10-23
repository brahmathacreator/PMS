package services.Interface;

import com.google.inject.ImplementedBy;
import models.Comments;
import models.Project;
import models.SubComments;
import play.mvc.Http;
import services.Implementation.ProjectImpl;

import java.util.Optional;

@ImplementedBy(ProjectImpl.class)
public interface ProjectMeta extends Generic<Project> {

    boolean saveComments(Comments object, Integer curdOpt, Http.Context ctx) throws Exception;

    Optional<Comments> getCommentDataByKey(Long id, Integer curdOpt) throws Exception;

    boolean saveSubComments(SubComments object, Integer curdOpt, Http.Context ctx) throws Exception;

    Project completeProjectByKey(Long id, Integer curdOpt) throws Exception;

}
