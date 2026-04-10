/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.werehouse;

import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author mateu
 */
public class SortingService {
   
/*  Poniżej prototyp statycznego sortowania paczek z podziałem na stacki.
    System.outy i pętlę while wykorzystałem do testowania, narazie zostawię.
    Narazie działa przez wpisywanie wymiarów z palca, ale to dobra baza do późniejszego rozwoju.
    Przyjąłem 2 jako minimalny wymiar paczki.
*/
    
    public SortingService() {
    }
    public void Sorting(){
        double p[] = new double[3];
        short stacks = 0;
        Scanner scanner = new Scanner(System.in);
        while(true){
            Arrays.fill(p, 0.0);
            System.out.println("\nPodaj dlugosc:");
            p[0] = scanner.nextDouble();
            System.out.println("\nPodaj szerokosc:");
            p[1] = scanner.nextDouble();
            System.out.println("\nPodaj wysokosc:");
            p[2] = scanner.nextDouble();

//            System.out.println("\nWymiary zapisane w tablicy:");
//            for (int i = 0; i < p.length; i++) {
//                System.out.println("Element " + i + ": " + p[i]);
//            }
            boolean isBaseOk = (p[0] >= 2 && p[0] <= 60) && (p[1] >= 2 && p[1] <= 40);
            if (!isBaseOk || p[2] < 2 || p[2] > 40) {
                System.out.println("Bledne wymiary!");
                continue;
            }
            if(p[2] <= 10){
                System.out.println("Paczka zajmuje > 1 < stack");
                stacks = 1;
            }else if(p[2] <= 20){
                System.out.println("Paczka zajmuje > 2 < stacki");
                stacks = 2;
            }else{
                System.out.println("Paczka zajmuje > 4 < stacki");
                stacks = 4;
            }
        }
    }
}
