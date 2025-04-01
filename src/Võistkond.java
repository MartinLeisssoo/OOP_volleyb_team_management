/**
 * Klass esindab võrkpallivõistkonda, mis koosneb mängijatest.
 */
public class Võistkond {
    // Väljad
    private String nimi;
    private Mängija[] mängijad;
    private int mängijateArv;

    /**
     * Konstruktor uue võistkonna loomiseks.
     */
    public Võistkond(String nimi, int maksimumMängijad) {
        this.nimi = nimi;
        this.mängijad = new Mängija[maksimumMängijad];
        this.mängijateArv = 0;
    }

    /**
     * Lisab mängija võistkonda.
     */
    public boolean lisaMängija(Mängija mängija) {
        if (mängijateArv < mängijad.length) {
            mängijad[mängijateArv] = mängija;
            mängijateArv++;
            return true;
        }
        return false;
    }

    /**
     * Leiab mängija nime järgi.
     * tagastab Mängija objekti või null, kui mängijat ei leitud
     */
    public Mängija leiaMängijaNimeJärgi(String nimi) {
        for (int i = 0; i < mängijateArv; i++) {
            if (mängijad[i].getNimi().equalsIgnoreCase(nimi)) {
                return mängijad[i];
            }
        }
        return null;
    }

    /**
     * Tagastab kõik võistkonna mängijad.
     */
    public Mängija[] getKõikMängijad() {
        Mängija[] tulemus = new Mängija[mängijateArv];
        for (int i = 0; i < mängijateArv; i++) {
            tulemus[i] = mängijad[i];
        }
        return tulemus;
    }

    public int getMängijateArv() {
        return mängijateArv;
    }

    public String getNimi() {
        return nimi;
    }
}