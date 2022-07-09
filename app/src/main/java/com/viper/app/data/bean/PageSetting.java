package com.viper.app.data.bean;

import com.viper.app.util.U;
import com.viper.app.R;

public class PageSetting {
    private long id;
    private String pageName;
    private String dataSource;
    private int readMode;
    public PageSetting() {
        this.pageName = U.getSting(R.string.opc_def_page_name);
        this.dataSource = U.getSting(R.string.opc_def_uri);
        this.id = System.currentTimeMillis();
        this.readMode = 0;
    }
    public PageSetting(Long id) {
        this.pageName = U.getSting(R.string.opc_def_page_name);
        this.dataSource = U.getSting(R.string.opc_def_uri);
        this.id = id;
        this.readMode = 0;
    }


    public PageSetting(String pageName, String dataSource) {
        this.pageName = pageName;
        this.dataSource = dataSource;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public long getId() {
        return id;
    }

    public int getReadMode() {
        return readMode;
    }

    public void setReadMode(int readMode) {
        this.readMode = readMode;
    }

    public void setId(long id) {
        this.id = id;
    }
}
