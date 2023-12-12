package mp.dao;

import mp.constants.Strings;
import mp.dao.impl.*;
import mp.dao.interfaces.*;
import java.util.logging.Logger;

public class DaoProvider {
    private static final Logger LOGGER = Logger.getLogger(DaoProvider.class.getName());
    private static final IUserDao userDao = null;
    private static final IAppDao appDao = null;
    private static final IAppLoginDao appLoginDao = null;
    private static final IAppQuestionDao appQuestionDao = null;
    private static final IAppNoteDao appNoteDao = null;

    private DaoProvider() {

    }

    public static IUserDao getUserDao() {
        return getInstance(userDao, UserDao.class);
    }
    public static IAppDao getAppDao() {
        return getInstance(appDao, AppDao.class);
    }
    public static IAppLoginDao getAppLoginDao() {
        return getInstance(appLoginDao, AppLoginDao.class);
    }
    public static IAppQuestionDao getAppQuestionDao() {
        return getInstance(appQuestionDao, AppQuestionDao.class);
    }
    public static IAppNoteDao getAppNoteDao() {
        return getInstance(appNoteDao, AppNoteDao.class);
    }

    private static <T> T getInstance(Object instance, Class<T> daoClass) {
        if (instance == null) {
            try {
                instance = daoClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                String errorMessage = String.format(Strings.Errors.CREATING_NEW_INSTANCE, daoClass.getName(), e.getMessage());
                LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
                e.printStackTrace();
            }
        }
        return (T)instance;
    }


}
