

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.feature.package_.PackageService;
import com.mycompany.projekt_io.feature.warehouse.SortingService;
import com.mycompany.projekt_io.feature.warehouse.SortingServiceInterface;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoadTest {

    // ── JEDNA ZMIENNA DLA WSZYSTKICH TESTÓW ──────────────────────────────────
    private static final int LOAD_SIZE = 1;
    // ─────────────────────────────────────────────────────────────────────────

    private static final PackageDAO packageDAO = new PackageDAO();
    private static final PackageService packageService = new PackageService();
    private static final UserDAO userDAO = new UserDAO();

    // ── POMOCNICZA: dodaje jedną paczkę testową ───────────────────────────────
    private void addTestPackage(int index) {
        packageService.addPackageFull(
            null,
            "Warszawa", "Kraków",
            5.0, 15.0, 15.0, 15.0,
            "Nadawca_" + index, "Warszawa", "Ulica " + index,
            "00-001", "nadawca" + index + "@test.pl", "111222333",
            "Odbiorca_" + index, "Kraków", "Aleja " + index,
            "30-001", "odbiorca" + index + "@test.pl", "444555666"
        );
    }

    // ── POMOCNICZA: dodaje jednego użytkownika testowego ─────────────────────
    private void addTestUser(int index) {
        userDAO.addUser(
            "testuser_" + index,
            "$2a$12$dummyhashfortestingpurposesonly" + index,
            1
        );
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TESTY JEDNOSTKOWE
    // ═════════════════════════════════════════════════════════════════════════

    @Test
    @Order(1)
    public void testAddPackages() {
        System.out.println("\n=== TEST 1: DODAWANIE " + LOAD_SIZE + " PACZEK ===");
        long start = System.currentTimeMillis();

        for (int i = 0; i < LOAD_SIZE; i++) {
            addTestPackage(i);
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("Dodano paczek:     " + LOAD_SIZE);
        System.out.println("Czas:              " + time + " ms");
        System.out.println("Średnio na paczkę: " + (time / LOAD_SIZE) + " ms");
    }

    @Test
    @Order(2)
    public void testAddUsers() {
        System.out.println("\n=== TEST 2: DODAWANIE " + LOAD_SIZE + " UŻYTKOWNIKÓW ===");
        long start = System.currentTimeMillis();

        for (int i = 0; i < LOAD_SIZE; i++) {
            addTestUser(i);
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("Dodano użytkowników: " + LOAD_SIZE);
        System.out.println("Czas:                " + time + " ms");
        System.out.println("Średnio na rekord:   " + (time / LOAD_SIZE) + " ms");
    }

    @Test
    @Order(3)
    public void testGetPackages() {
        System.out.println("\n=== TEST 3: POBIERANIE LISTY PACZEK ===");
        long start = System.currentTimeMillis();

        List<Package> packages = packageDAO.getPackages();

        long time = System.currentTimeMillis() - start;
        System.out.println("Pobrano paczek: " + packages.size());
        System.out.println("Czas:           " + time + " ms");
    }

    @Test
    @Order(4)
    public void testGetUsers() {
        System.out.println("\n=== TEST 4: POBIERANIE LISTY UŻYTKOWNIKÓW ===");
        long start = System.currentTimeMillis();

        List<User> users = userDAO.getUsers();

        long time = System.currentTimeMillis() - start;
        System.out.println("Pobrano użytkowników: " + users.size());
        System.out.println("Czas:                 " + time + " ms");
    }

    @Test
    @Order(5)
    public void testGetSinglePackage() {
        System.out.println("\n=== TEST 5: POBIERANIE POJEDYNCZYCH PACZEK (" + LOAD_SIZE + "x) ===");
        List<Package> all = packageDAO.getPackages();
        int limit = Math.min(LOAD_SIZE, all.size());

        long start = System.currentTimeMillis();

        for (int i = 0; i < limit; i++) {
            packageDAO.getPackage(all.get(i).getPackage_id());
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("Pobrano pojedynczo: " + limit);
        System.out.println("Czas:               " + time + " ms");
        System.out.println("Średnio na zapytanie: " + (time / Math.max(limit, 1)) + " ms");
    }

    @Test
    @Order(6)
    public void testSortingAlgorithm() {
        System.out.println("\n=== TEST 6: ALGORYTM SORTOWANIA ===");
        List<Package> before = packageDAO.getPackages();
        long unassigned = before.stream()
            .filter(p -> p.getPackage_rack() == null || p.getPackage_rack().getRack_id() <= 0)
            .count();

        long start = System.currentTimeMillis();

        SortingServiceInterface sorting = new SortingService(packageDAO);
        sorting.assignShelvesToPackages();

        long time = System.currentTimeMillis() - start;
        System.out.println("Paczek przed sortowaniem: " + before.size());
        System.out.println("Nieprzypisanych:          " + unassigned);
        System.out.println("Czas sortowania:          " + time + " ms");
    }

    @Test
    @Order(7)
    public void testUpdatePackages() {
        System.out.println("\n=== TEST 7: AKTUALIZACJA " + LOAD_SIZE + " PACZEK ===");
        List<Package> packages = packageDAO.getPackages();
        int limit = Math.min(LOAD_SIZE, packages.size());

        long start = System.currentTimeMillis();

        for (int i = 0; i < limit; i++) {
            Package p = packages.get(i);
            p.setWidth(20);
            p.setHeight(20);
            p.setDepth(20);
            packageDAO.changePackage(p);
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("Zaktualizowano paczek: " + limit);
        System.out.println("Czas:                  " + time + " ms");
        System.out.println("Średnio na aktualizację: " + (time / Math.max(limit, 1)) + " ms");
    }

    @Test
    @Order(8)
    public void testDeletePackages() {
        System.out.println("\n=== TEST 8: USUWANIE " + LOAD_SIZE + " PACZEK ===");
        List<Package> packages = packageDAO.getPackages();
        int limit = Math.min(LOAD_SIZE, packages.size());

        long start = System.currentTimeMillis();

        for (int i = 0; i < limit; i++) {
            packageDAO.deletePackage(packages.get(i).getPackage_id());
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("Usunięto paczek: " + limit);
        System.out.println("Czas:            " + time + " ms");
        System.out.println("Średnio na usunięcie: " + (time / Math.max(limit, 1)) + " ms");
    }

    @Test
    @Order(9)
    public void testDeleteUsers() {
        System.out.println("\n=== TEST 9: USUWANIE UŻYTKOWNIKÓW TESTOWYCH ===");
        List<User> users = userDAO.getUsers();
        int count = 0;

        long start = System.currentTimeMillis();

        for (User u : users) {
            if (u.getLogin().startsWith("testuser_")) {
                userDAO.deleteUser(u.getUser_id());
                count++;
            }
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("Usunięto użytkowników: " + count);
        System.out.println("Czas:                  " + time + " ms");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TEST KOMPLEKSOWY — wywołuje wszystkie operacje na przemiennie
    // ═════════════════════════════════════════════════════════════════════════

    @Test
    @Order(10)
    public void testFullLoadCycle() {
        System.out.println("\n=== TEST 10: PEŁNY CYKL OBCIĄŻENIOWY (" + LOAD_SIZE + " iteracji) ===");
        long totalStart = System.currentTimeMillis();

        for (int i = 0; i < LOAD_SIZE; i++) {

            // 1. Dodaj paczkę
            addTestPackage(i + 10000);

            // 2. Co 10 iteracji pobierz całą listę
            if (i % 10 == 0) {
                packageDAO.getPackages();
                System.out.println("  iteracja " + i + " — pobrano listę paczek");
            }

            // 3. Co 20 iteracji dodaj użytkownika
            if (i % 20 == 0) {
                addTestUser(i + 10000);
                System.out.println("  iteracja " + i + " — dodano użytkownika");
            }

            // 4. Co 25 iteracji uruchom sortowanie
            if (i % 25 == 0) {
                SortingServiceInterface sorting = new SortingService(packageDAO);
                sorting.assignShelvesToPackages();
                System.out.println("  iteracja " + i + " — uruchomiono sortowanie");
            }

            // 5. Co 30 iteracji usuń najstarszą paczkę
            if (i % 30 == 0) {
                List<Package> current = packageDAO.getPackages();
                if (!current.isEmpty()) {
                    packageDAO.deletePackage(current.get(0).getPackage_id());
                    System.out.println("  iteracja " + i + " — usunięto paczkę");
                }
            }
        }

        long totalTime = System.currentTimeMillis() - totalStart;
        List<Package> finalList = packageDAO.getPackages();

        System.out.println("\n--- PODSUMOWANIE CYKLU ---");
        System.out.println("Iteracji:          " + LOAD_SIZE);
        System.out.println("Paczek w bazie:    " + finalList.size());
        System.out.println("Łączny czas:       " + totalTime + " ms");
        System.out.println("Średnio/iterację:  " + (totalTime / LOAD_SIZE) + " ms");
    }
}
