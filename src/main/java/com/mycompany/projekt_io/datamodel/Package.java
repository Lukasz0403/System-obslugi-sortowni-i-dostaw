package com.mycompany.projekt_io.datamodel;

/**
 * Model danych reprezentujący paczkę w systemie magazynowym.
 * <p>
 * Przechowuje pełne informacje o paczce: dane nadawcy, odbiorcy, regionów
 * nadania i docelowego, przypisanego gabarytu, regału magazynowego oraz
 * rzeczywistych wymiarów i wagi paczki.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class Package {

    /**
     * Unikalny identyfikator paczki.
     */
    int package_id;

    /**
     * Nadawca paczki.
     */
    Sender package_sender;

    /**
     * Odbiorca paczki.
     */
    Recipient package_recipient;

    /**
     * Region kurierski nadania paczki.
     */
    Region package_region;

    /**
     * Docelowy region kurierski dla paczki.
     */
    Region package_dest_region;

    /**
     * Gabaryt paczki określający dopuszczalne limity.
     */
    Format package_format;

    /**
     * Regał magazynowy przypisany do paczki.
     */
    Rack package_rack;

    /**
     * Rzeczywista szerokość paczki w centymetrach.
     */
    int width;

    /**
     * Rzeczywista wysokość paczki w centymetrach.
     */
    int height;

    /**
     * Rzeczywista głębokość paczki w centymetrach.
     */
    int depth;

    /**
     * Rzeczywista waga paczki w kilogramach.
     */
    int weight;

    /**
     * Tworzy obiekt paczki z pełnymi danymi.
     *
     * @param package_id unikalny identyfikator paczki
     * @param package_sender nadawca paczki
     * @param package_recipient odbiorca paczki
     * @param package_region region kurierski nadania
     * @param package_dest_region region kurierski docelowy
     * @param package_format gabaryt paczki
     * @param package_rack regał, do którego przypisana jest paczka; może być
     * {@code null} jeśli paczka nie jest jeszcze przyjęta
     * @param width rzeczywista szerokość paczki w centymetrach
     * @param height rzeczywista wysokość paczki w centymetrach
     * @param depth rzeczywista głębokość paczki w centymetrach
     * @param weight rzeczywista waga paczki w kilogramach
     */
    public Package(int package_id, Sender package_sender, Recipient package_recipient,
            Region package_region, Region package_dest_region, Format package_format,
            Rack package_rack, int width, int height, int depth, int weight) {
        this.package_id = package_id;
        this.package_sender = package_sender;
        this.package_recipient = package_recipient;
        this.package_region = package_region;
        this.package_dest_region = package_dest_region;
        this.package_format = package_format;
        this.package_rack = package_rack;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.weight = weight;
    }

    /**
     * Zwraca unikalny identyfikator paczki.
     *
     * @return unikalny identyfikator paczki
     */
    public int getPackage_id() {
        return package_id;
    }

    /**
     * Zwraca dane nadawcy paczki.
     *
     * @return nadawca paczki
     */
    public Sender getPackage_sender() {
        return package_sender;
    }

    /**
     * Zwraca dane odbiorcy paczki.
     *
     * @return odbiorca paczki
     */
    public Recipient getPackage_recipient() {
        return package_recipient;
    }

    /**
     * Zwraca region kurierski nadania paczki.
     *
     * @return region kurierski nadania
     */
    public Region getPackage_region() {
        return package_region;
    }

    /**
     * Zwraca docelowy region kurierski dla paczki.
     *
     * @return region kurierski docelowy
     */
    public Region getPackage_dest_region() {
        return package_dest_region;
    }

    /**
     * Zwraca przypisany gabaryt paczki.
     *
     * @return gabaryt paczki
     */
    public Format getPackage_format() {
        return package_format;
    }

    /**
     * Zwraca regał przypisany do paczki w magazynie.
     *
     * @return regał przypisany do paczki lub {@code null} jeśli brak
     */
    public Rack getPackage_rack() {
        return package_rack;
    }

    /**
     * Zwraca rzeczywistą szerokość paczki.
     *
     * @return rzeczywista szerokość paczki w centymetrach
     */
    public int getWidth() {
        return width;
    }

    /**
     * Zwraca rzeczywistą wysokość paczki.
     *
     * @return rzeczywista wysokość paczki w centymetrach
     */
    public int getHeight() {
        return height;
    }

    /**
     * Zwraca rzeczywistą głębokość paczki.
     *
     * @return rzeczywista głębokość paczki w centymetrach
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Zwraca rzeczywistą wagę paczki.
     *
     * @return rzeczywista waga paczki w kilogramach
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Ustawia unikalny identyfikator paczki.
     *
     * @param package_id unikalny identyfikator paczki
     */
    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    /**
     * Ustawia nadawcę paczki.
     *
     * @param package_sender nadawca paczki
     */
    public void setPackage_sender(Sender package_sender) {
        this.package_sender = package_sender;
    }

    /**
     * Ustawia odbiorcę paczki.
     *
     * @param package_recipient odbiorca paczki
     */
    public void setPackage_recipient(Recipient package_recipient) {
        this.package_recipient = package_recipient;
    }

    /**
     * Ustawia region kurierski nadania paczki.
     *
     * @param package_region region kurierski nadania
     */
    public void setPackage_region(Region package_region) {
        this.package_region = package_region;
    }

    /**
     * Ustawia docelowy region kurierski dla paczki.
     *
     * @param package_dest_region region kurierski docelowy
     */
    public void setPackage_dest_region(Region package_dest_region) {
        this.package_dest_region = package_dest_region;
    }

    /**
     * Ustawia gabaryt paczki.
     *
     * @param package_format gabaryt paczki
     */
    public void setPackage_format(Format package_format) {
        this.package_format = package_format;
    }

    /**
     * Przypisuje paczkę do konkretnego regału magazynowego.
     *
     * @param package_rack regał przypisany do paczki; może być {@code null}
     */
    public void setPackage_rack(Rack package_rack) {
        this.package_rack = package_rack;
    }

    /**
     * Ustawia rzeczywistą szerokość paczki.
     *
     * @param width rzeczywista szerokość paczki w centymetrach
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Ustawia rzeczywistą wysokość paczki.
     *
     * @param height rzeczywista wysokość paczki w centymetrach
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Ustawia rzeczywistą głębokość paczki.
     *
     * @param depth rzeczywista głębokość paczki w centymetrach
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Ustawia rzeczywistą wagę paczki.
     *
     * @param weight rzeczywista waga paczki w kilogramach
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Package{" + "package_id=" + package_id + ", package_sender=" + package_sender
                + ", package_recipient=" + package_recipient + ", package_region=" + package_region
                + ", package_dest_region=" + package_dest_region + ", package_format=" + package_format
                + ", package_rack=" + package_rack + ", width=" + width + ", height=" + height
                + ", depth=" + depth + ", weight=" + weight + '}';
    }
}
