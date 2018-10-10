package services.Interface;

import com.google.inject.ImplementedBy;
import models.Comments;
import models.Project;
import play.mvc.Http;
import services.Implementation.ProjectImpl;

@ImplementedBy(ProjectImpl.class)
public interface ProjectMeta extends Generic<Project> {

    boolean saveComments(Comments object, Integer curdOpt, Http.Context ctx) throws Exception;

}
