package com.learn.mall.rbac.model;

import java.time.LocalDateTime;

public class Menu {

    private Long menuId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long parentId;
    private Integer bizType;
    private String permission;
    private String path;
    private String component;
    private String redirect;
    private Integer alwaysShow;
    private Integer hidden;
    private String name;
    private String title;
    private String icon;
    private Integer noCache;
    private Integer breadcrumb;
    private Integer affix;
    private String activeMenu;
    private Integer seq;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public Integer getAlwaysShow() {
        return alwaysShow;
    }

    public void setAlwaysShow(Integer alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public Integer getHidden() {
        return hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getNoCache() {
        return noCache;
    }

    public void setNoCache(Integer noCache) {
        this.noCache = noCache;
    }

    public Integer getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(Integer breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public Integer getAffix() {
        return affix;
    }

    public void setAffix(Integer affix) {
        this.affix = affix;
    }

    public String getActiveMenu() {
        return activeMenu;
    }

    public void setActiveMenu(String activeMenu) {
        this.activeMenu = activeMenu;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
