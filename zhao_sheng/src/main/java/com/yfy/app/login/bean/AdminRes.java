package com.yfy.app.login.bean;

import com.yfy.app.login.bean.ClassBean;

import java.util.List;

/**
 * Created by yfy1 on 2016/11/30.
 */
public class AdminRes {
    private String result;
    private String error_code;
    private String isheadmasters;//校长权限
    private String isassessadmin;
    private String isclassmaster;
    private String isstuillcheck;
    private String ishqadmin;//
    private String isnoticeadmin;
    private String isqjadmin;
    private String isxcadmin;//
    private String username;
    private String isfuncRoom;
    private String ishqlader;
    private String islogistics;//后勤
    private List<ClassBean> classinfo;
    private String isdutyreport;//值周
    private String iseventadmin;//值周
    private String isoffice_supply;//物品申领审核、新增物品，库存审核，物品入库嫩修改数量、分校申领审核
    private String isoffice_supply_master;//是否有审核新物品权限,采购审核
    private String issupplycount;//物品入库嫩修改数量(记录：isoffice_supply，可进)
    private String issignetadmin;//印章审核权限
    private String iselectiveadmin;//延时服务权限


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getIsheadmasters() {
        return isheadmasters;
    }

    public void setIsheadmasters(String isheadmasters) {
        this.isheadmasters = isheadmasters;
    }

    public String getIselectiveadmin() {
        return iselectiveadmin;
    }

    public void setIselectiveadmin(String iselectiveadmin) {
        this.iselectiveadmin = iselectiveadmin;
    }

    public String getIssignetadmin() {
        return issignetadmin;
    }

    public void setIssignetadmin(String issignetadmin) {
        this.issignetadmin = issignetadmin;
    }

    public String getIsclassmaster() {
        return isclassmaster;
    }

    public void setIsclassmaster(String isclassmaster) {
        this.isclassmaster = isclassmaster;
    }


    public String getIsstuillcheck() {
        return isstuillcheck;
    }

    public void setIsstuillcheck(String isstuillcheck) {
        this.isstuillcheck = isstuillcheck;
    }

    public String getIssupplycount() {
        return issupplycount;
    }

    public void setIssupplycount(String issupplycount) {
        this.issupplycount = issupplycount;
    }

    public List<ClassBean> getClassinfo() {
        return classinfo;
    }

    public void setClassinfo(List<ClassBean> classinfo) {
        this.classinfo = classinfo;
    }

    public String getIseventadmin() {
        return iseventadmin;
    }

    public void setIseventadmin(String iseventadmin) {
        this.iseventadmin = iseventadmin;
    }

    public String getIsdutyreport() {
        return isdutyreport;
    }

    public void setIsdutyreport(String isdutyreport) {
        this.isdutyreport = isdutyreport;
    }

    public String getIsoffice_supply_master() {
        return isoffice_supply_master;
    }

    public void setIsoffice_supply_master(String isoffice_supply_master) {
        this.isoffice_supply_master = isoffice_supply_master;
    }

    public String getIsoffice_supply() {
        return isoffice_supply;
    }

    public void setIsoffice_supply(String isoffice_supply) {
        this.isoffice_supply = isoffice_supply;
    }

    public String getIslogistics() {
        return islogistics;
    }

    public void setIslogistics(String islogistics) {
        this.islogistics = islogistics;
    }

    public String getIshqlader() {
        return ishqlader;
    }

    public void setIshqlader(String ishqlader) {
        this.ishqlader = ishqlader;
    }

    public String getIsfuncRoom() {
        return isfuncRoom;
    }

    public void setIsfuncRoom(String isfuncRoom) {
        this.isfuncRoom = isfuncRoom;
    }

    public String getIsassessadmin() {
        return isassessadmin;
    }

    public void setIsassessadmin(String isassessadmin) {
        this.isassessadmin = isassessadmin;
    }

    public String getIshqadmin() {
        return ishqadmin;
    }

    public void setIshqadmin(String ishqadmin) {
        this.ishqadmin = ishqadmin;
    }

    public String getIsnoticeadmin() {
        return isnoticeadmin;
    }

    public void setIsnoticeadmin(String isnoticeadmin) {
        this.isnoticeadmin = isnoticeadmin;
    }

    public String getIsqjadmin() {
        return isqjadmin;
    }

    public void setIsqjadmin(String isqjadmin) {
        this.isqjadmin = isqjadmin;
    }

    public String getIsxcadmin() {
        return isxcadmin;
    }

    public void setIsxcadmin(String isxcadmin) {
        this.isxcadmin = isxcadmin;
    }

    public String  getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
