package com.yfy.greendao;



import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AdminDb {
    @NotNull
    private String isheadmasters;//校长权限
    @NotNull
    private String isassessadmin;
    @NotNull
    private String isclassmaster;
    @NotNull
    private String isstuillcheck;
    @NotNull
    private String ishqadmin;//
    @NotNull
    private String isnoticeadmin;
    @NotNull
    private String isqjadmin;
    @NotNull
    private String isxcadmin;//
    @NotNull
    private String isfuncRoom;
    @NotNull
    private String ishqlader;
    @NotNull
    private String islogistics;//后勤
    @NotNull
    private String classinfo;
    @NotNull
    private String isdutyreport;//值周
    @NotNull
    private String iseventadmin;//值周
    @NotNull
    private String isoffice_supply;//物品申领审核、新增物品，库存审核，物品入库嫩修改数量、分校申领审核
    @NotNull
    private String isoffice_supply_master;//是否有审核新物品权限,采购审核
    @NotNull
    private String issupplycount;//物品入库嫩修改数量(记录：isoffice_supply，可进)
    @NotNull
    private String issignetadmin;//印章审核权限
    @NotNull
    private String iselectiveadmin;//延时服务权限
    @Id
    private Long id;
    @Generated(hash = 284699972)
    public AdminDb(@NotNull String isheadmasters, @NotNull String isassessadmin,
            @NotNull String isclassmaster, @NotNull String isstuillcheck,
            @NotNull String ishqadmin, @NotNull String isnoticeadmin,
            @NotNull String isqjadmin, @NotNull String isxcadmin,
            @NotNull String isfuncRoom, @NotNull String ishqlader,
            @NotNull String islogistics, @NotNull String classinfo,
            @NotNull String isdutyreport, @NotNull String iseventadmin,
            @NotNull String isoffice_supply, @NotNull String isoffice_supply_master,
            @NotNull String issupplycount, @NotNull String issignetadmin,
            @NotNull String iselectiveadmin, Long id) {
        this.isheadmasters = isheadmasters;
        this.isassessadmin = isassessadmin;
        this.isclassmaster = isclassmaster;
        this.isstuillcheck = isstuillcheck;
        this.ishqadmin = ishqadmin;
        this.isnoticeadmin = isnoticeadmin;
        this.isqjadmin = isqjadmin;
        this.isxcadmin = isxcadmin;
        this.isfuncRoom = isfuncRoom;
        this.ishqlader = ishqlader;
        this.islogistics = islogistics;
        this.classinfo = classinfo;
        this.isdutyreport = isdutyreport;
        this.iseventadmin = iseventadmin;
        this.isoffice_supply = isoffice_supply;
        this.isoffice_supply_master = isoffice_supply_master;
        this.issupplycount = issupplycount;
        this.issignetadmin = issignetadmin;
        this.iselectiveadmin = iselectiveadmin;
        this.id = id;
    }
    @Generated(hash = 909522727)
    public AdminDb() {
    }
    public AdminDb(Long id) {
        this.id = id;
    }
    public String getIsheadmasters() {
        return this.isheadmasters;
    }
    public void setIsheadmasters(String isheadmasters) {
        this.isheadmasters = isheadmasters;
    }
    public String getIsassessadmin() {
        return this.isassessadmin;
    }
    public void setIsassessadmin(String isassessadmin) {
        this.isassessadmin = isassessadmin;
    }
    public String getIsclassmaster() {
        return this.isclassmaster;
    }
    public void setIsclassmaster(String isclassmaster) {
        this.isclassmaster = isclassmaster;
    }
    public String getIsstuillcheck() {
        return this.isstuillcheck;
    }
    public void setIsstuillcheck(String isstuillcheck) {
        this.isstuillcheck = isstuillcheck;
    }
    public String getIshqadmin() {
        return this.ishqadmin;
    }
    public void setIshqadmin(String ishqadmin) {
        this.ishqadmin = ishqadmin;
    }
    public String getIsnoticeadmin() {
        return this.isnoticeadmin;
    }
    public void setIsnoticeadmin(String isnoticeadmin) {
        this.isnoticeadmin = isnoticeadmin;
    }
    public String getIsqjadmin() {
        return this.isqjadmin;
    }
    public void setIsqjadmin(String isqjadmin) {
        this.isqjadmin = isqjadmin;
    }
    public String getIsxcadmin() {
        return this.isxcadmin;
    }
    public void setIsxcadmin(String isxcadmin) {
        this.isxcadmin = isxcadmin;
    }

    public String getIsfuncRoom() {
        return this.isfuncRoom;
    }
    public void setIsfuncRoom(String isfuncRoom) {
        this.isfuncRoom = isfuncRoom;
    }
    public String getIshqlader() {
        return this.ishqlader;
    }
    public void setIshqlader(String ishqlader) {
        this.ishqlader = ishqlader;
    }
    public String getIslogistics() {
        return this.islogistics;
    }
    public void setIslogistics(String islogistics) {
        this.islogistics = islogistics;
    }
    public String getClassinfo() {
        return this.classinfo;
    }
    public void setClassinfo(String classinfo) {
        this.classinfo = classinfo;
    }
    public String getIsdutyreport() {
        return this.isdutyreport;
    }
    public void setIsdutyreport(String isdutyreport) {
        this.isdutyreport = isdutyreport;
    }
    public String getIseventadmin() {
        return this.iseventadmin;
    }
    public void setIseventadmin(String iseventadmin) {
        this.iseventadmin = iseventadmin;
    }
    public String getIsoffice_supply() {
        return this.isoffice_supply;
    }
    public void setIsoffice_supply(String isoffice_supply) {
        this.isoffice_supply = isoffice_supply;
    }
    public String getIsoffice_supply_master() {
        return this.isoffice_supply_master;
    }
    public void setIsoffice_supply_master(String isoffice_supply_master) {
        this.isoffice_supply_master = isoffice_supply_master;
    }
    public String getIssupplycount() {
        return this.issupplycount;
    }
    public void setIssupplycount(String issupplycount) {
        this.issupplycount = issupplycount;
    }
    public String getIssignetadmin() {
        return this.issignetadmin;
    }
    public void setIssignetadmin(String issignetadmin) {
        this.issignetadmin = issignetadmin;
    }
    public String getIselectiveadmin() {
        return this.iselectiveadmin;
    }
    public void setIselectiveadmin(String iselectiveadmin) {
        this.iselectiveadmin = iselectiveadmin;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
