package com.mycompany.projekt_io.datamodel;

/**
 * Model danych reprezentujący gabaryt paczki.
 * <p>
 * Definiuje maksymalne dopuszczalne wymiary i wagę dla danego gabarytu
 * oraz liczbę slotów magazynowych zajmowanych przez paczkę tego formatu.
 * System obsługuje trzy gabaryty: A, B i C.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class Format {

    String format_id;
    int max_format_width, max_format_height, max_format_depth, max_wage, slot_coverage;

    /**
     * Tworzy obiekt gabarytu paczki.
     *
     * @param format_id        identyfikator gabarytu ("A", "B" lub "C")
     * @param max_format_width maksymalna szerokość paczki w centymetrach
     * @param max_format_height maksymalna wysokość paczki w centymetrach
     * @param max_format_depth maksymalna głębokość paczki w centymetrach
     * @param max_wage         maksymalna waga paczki w kilogramach
     * @param slot_coverage    liczba slotów magazynowych zajmowanych przez paczkę
     */
    public Format(String format_id, int max_format_width, int max_format_height,
                  int max_format_depth, int max_wage, int slot_coverage) {
        this.format_id = format_id;
        this.max_format_width = max_format_width;
        this.max_format_height = max_format_height;
        this.max_format_depth = max_format_depth;
        this.max_wage = max_wage;
        this.slot_coverage = slot_coverage;
    }

    /** @return identyfikator gabarytu ("A", "B" lub "C") */
    public String getFormat_id() { return format_id; }

    /** @return maksymalna szerokość paczki w centymetrach */
    public int getMax_format_width() { return max_format_width; }

    /** @return maksymalna wysokość paczki w centymetrach */
    public int getMax_format_height() { return max_format_height; }

    /** @return maksymalna głębokość paczki w centymetrach */
    public int getMax_format_depth() { return max_format_depth; }

    /** @return maksymalna waga paczki w kilogramach */
    public int getMax_wage() { return max_wage; }

    /** @return liczba slotów magazynowych zajmowanych przez paczkę */
    public int getSlot_coverage() { return slot_coverage; }

    /** @param format_id identyfikator gabarytu */
    public void setFormat_id(String format_id) { this.format_id = format_id; }

    /** @param max_format_width maksymalna szerokość w centymetrach */
    public void setMax_format_width(int max_format_width) { this.max_format_width = max_format_width; }

    /** @param max_format_height maksymalna wysokość w centymetrach */
    public void setMax_format_height(int max_format_height) { this.max_format_height = max_format_height; }

    /** @param max_format_depth maksymalna głębokość w centymetrach */
    public void setMax_format_depth(int max_format_depth) { this.max_format_depth = max_format_depth; }

    /** @param max_wage maksymalna waga w kilogramach */
    public void setMax_wage(int max_wage) { this.max_wage = max_wage; }

    /** @param slot_coverage liczba zajmowanych slotów magazynowych */
    public void setSlot_coverage(int slot_coverage) { this.slot_coverage = slot_coverage; }

    @Override
    public String toString() {
        return "Format{" + "format_id=" + format_id + ", max_format_width=" + max_format_width
                + ", max_format_height=" + max_format_height + ", max_format_depth=" + max_format_depth
                + ", max_wage=" + max_wage + '}';
    }
}