package controllers.utils;

import controllers.constants.Constants;
import controllers.dto.Navigation;
import play.Logger;
import play.mvc.Http;

import java.io.File;

public class Utils {

    public static boolean checkFileSize(File f, long limit) throws Exception {
        Logger.debug("Inside [Utils][getFileSize]");
        return f.length() < (limit * (1024 * 1024)) ? true : false;
    }


}
