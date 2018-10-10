package controllers.security;

import play.mvc.Controller;
import play.mvc.Security;

@Security.Authenticated(SessionMgmt.class)
public class SessionController extends Controller {
}
