
import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.feature.package_.PackageService;
import com.mycompany.projekt_io.feature.warehouse.SortingService;
import com.mycompany.projekt_io.feature.warehouse.SortingServiceInterface;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import java.util.List;

/**
 * Testy wydajnościowe systemu obsługi sortowni.
 * <p>
 * Mierzą czasy wykonania kluczowych operacji na bazie danych przy różnych
 * poziomach obciążenia. Liczbę operacji dla wszystkich testów kontroluje stała
 * {@link #LOAD_SIZE}. Testy wymagają aktywnego połączenia z bazą danych i są
 * wykonywane w określonej kolejności zdefiniowanej przez {@code @Order}.
 * </p>
 * 
 * @author Mateusz Gojny
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoadTest {

    /**
     * Liczba operacji wykonywana przez każdy test. Zmiana tej wartości wpływa
     * na wszystkie testy jednocześnie.
     */
    private static final int LOAD_SIZE = 1;

    private static final PackageDAO packageDAO = new PackageDAO();
    private static final PackageService packageService = new PackageService();
    private static final UserDAO userDAO = new UserDAO();

    /**
     * Dodaje testową paczkę do bazy danych z danymi nadawcy i odbiorcy
     * wygenerowanymi na podstawie podanego indeksu.
     *
     * @param index indeks używany do generowania unikalnych danych testowych
     */
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

    /**
     * Dodaje testowego użytkownika do bazy danych z loginem opartym na podanym
     * indeksie i fikcyjnym hashem BCrypt.
     *
     * @param index indeks używany do generowania unikalnego loginu
     */
    private void addTestUser(int index) {
        userDAO.addUser(
                "testuser_" + index,
                "$2a$12$dummyhashfortestingpurposesonly" + index,
                1
        );
    }

    /**
     * Mierzy czas dodania {@link #LOAD_SIZE} paczek do bazy danych.
     */
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

    /**
     * Mierzy czas dodania {@link #LOAD_SIZE} użytkowników do bazy danych.
     */
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

    /**
     * Mierzy czas pobrania pełnej listy paczek z bazy danych.
     */
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

    /**
     * Mierzy czas pobrania pełnej listy użytkowników z bazy danych.
     */
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

    /**
     * Mierzy czas {@link #LOAD_SIZE} pojedynczych zapytań o konkretną paczkę po
     * jej identyfikatorze.
     */
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

    /**
     * Mierzy czas wykonania algorytmu sortowania na aktualnej zawartości bazy.
     * Wyświetla liczbę paczek przed sortowaniem oraz liczbę nieprzypisanych.
     */
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

    /**
     * Mierzy czas aktualizacji danych {@link #LOAD_SIZE} paczek w bazie danych.
     */
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

    /**
     * Mierzy czas usunięcia {@link #LOAD_SIZE} paczek z bazy danych.
     */
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

    /**
     * Usuwa wszystkich użytkowników testowych z bazy danych — czyli tych
     * których login zaczyna się od "testuser_".
     */
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

    /**
     * Test kompleksowy wykonujący wszystkie operacje na przemiennie przez
     * {@link #LOAD_SIZE} iteracji.
     * <p>
     * Co 10 iteracji pobiera listę paczek, co 20 dodaje użytkownika, co 25
     * uruchamia algorytm sortowania, co 30 usuwa najstarszą paczkę. Na końcu
     * wyświetla podsumowanie z łącznym czasem i średnim czasem na iterację.
     * </p>
     */
    @Test
    @Order(10)
    public void testFullLoadCycle() {
        System.out.println("\n=== TEST 10: PEŁNY CYKL OBCIĄŻENIOWY (" + LOAD_SIZE + " iteracji) ===");
        long totalStart = System.currentTimeMillis();

        for (int i = 0; i < LOAD_SIZE; i++) {

            addTestPackage(i + 10000);

            if (i % 10 == 0) {
                packageDAO.getPackages();
                System.out.println("  iteracja " + i + " — pobrano listę paczek");
            }

            if (i % 20 == 0) {
                addTestUser(i + 10000);
                System.out.println("  iteracja " + i + " — dodano użytkownika");
            }

            if (i % 25 == 0) {
                SortingServiceInterface sorting = new SortingService(packageDAO);
                sorting.assignShelvesToPackages();
                System.out.println("  iteracja " + i + " — uruchomiono sortowanie");
            }

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
