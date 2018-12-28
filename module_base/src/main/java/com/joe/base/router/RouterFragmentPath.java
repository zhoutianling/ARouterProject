package com.joe.base.router;

/**
 * desc: RouterFragmentPath.java
 * author: Joe
 * created at: 2018/12/27 下午3:36
 * <p>
 * 注意：路劲至少要有两层，否则会编译失败
 */

public class RouterFragmentPath {
    public static class Home {
        private static final String INDEX = "/index";
        public static final String PAGER_INDEX = INDEX + "/index";
    }

    public static class Discover {
        private static final String DISCOVER = "/discover";
        public static final String PAGER_DISCOVERY = DISCOVER + "/discovery";
    }

    public static class About {
        private static final String ABOUT = "/about";
        public static final String PAGER_ABOUT = ABOUT + "/about";
    }
}
