package com.juaracoding.utils;

import com.juaracoding.model.Menu;
import com.juaracoding.model.Akses;
import com.juaracoding.model.MenuHeader;

import java.util.List;
import java.util.stream.Collectors;

public class GenerateMenuString {

    /**
     * Generates a string representation for a Menu object.
     *
     * @param menu the Menu object to represent as a string
     * @return formatted string with Menu details
     */
    public static String generateMenuString(Menu menu) {
        if (menu == null) {
            return "Menu is null";
        }
        return String.format("Menu [ID: %d, Name: %s, Path: %s, Endpoint: %s, CreatedBy: %d, IsDelete: %d]",
                menu.getIdMenu(),
                menu.getNamaMenu(),
                menu.getPathMenu(),
                menu.getEndPoint(),
                menu.getCreatedBy(),
                menu.getIsDelete());
    }

    /**
     * Generates a string representation for an Akses object.
     *
     * @param akses the Akses object to represent as a string
     * @return formatted string with Akses details
     */
    public static String generateAksesString(Akses akses) {
        if (akses == null) {
            return "Akses is null";
        }
        String menuIds = akses.getListMenuAkses() != null
                ? akses.getListMenuAkses().stream().map(Menu::getIdMenu).map(String::valueOf).collect(Collectors.joining(", "))
                : "No Menus";

        return String.format("Akses [ID: %d, Name: %s, Divisi: %s, CreatedBy: %d, IsDelete: %d, Menus: [%s]]",
                akses.getIdAkses(),
                akses.getNamaAkses(),
                akses.getDivisi() != null ? akses.getDivisi().toString() : "No Divisi",
                akses.getCreatedBy(),
                akses.getIsDelete(),
                menuIds);
    }

    /**
     * Generates a string representation for a MenuHeader object.
     *
     * @param menuHeader the MenuHeader object to represent as a string
     * @return formatted string with MenuHeader details
     */
    public static String generateMenuHeaderString(MenuHeader menuHeader) {
        if (menuHeader == null) {
            return "MenuHeader is null";
        }
        return String.format("MenuHeader [ID: %d, Name: %s, Description: %s, CreatedBy: %d, IsDelete: %d]",
                menuHeader.getIdMenuHeader(),
                menuHeader.getNamaMenuHeader(),
                menuHeader.getDeskripsiMenuHeader(),
                menuHeader.getCreatedBy(),
                menuHeader.getIsDelete());
    }

    /**
     * Generates string representations for a list of Menu objects.
     *
     * @param menuList the list of Menu objects
     * @return formatted string of all Menu objects
     */
    public static String generateMenuListString(List<Menu> menuList) {
        if (menuList == null || menuList.isEmpty()) {
            return "Menu list is empty or null";
        }
        return menuList.stream()
                .map(GenerateMenuString::generateMenuString)
                .collect(Collectors.joining("\n"));
    }
}
