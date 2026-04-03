/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.core.database;

import java.util.List;
import com.mycompany.projekt_io.datamodel.Package;

/**
 *
 * @author Radosław
 */
public interface PackageDAOInterface {
    
    List<Package> getPackages();
    Package getPackage(int id);
}
