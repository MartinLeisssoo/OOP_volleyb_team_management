/**
 * Klass esindab võrkpallimängijat koos tema oskuste ja ELO reitinguga.
 */
public class Mängija {
    private String nimi;
    private int elo;
    private int servimisOskus;
    private int ründamisOskus;
    private int blokeerimisOskus;
    private int kaitsmisOskus;


    public Mängija(String nimi, int servimisOskus, int ründamisOskus, int blokeerimisOskus, int kaitsmisOskus) {
        this.nimi = nimi;
        this.servimisOskus = servimisOskus;
        this.ründamisOskus = ründamisOskus;
        this.blokeerimisOskus = blokeerimisOskus;
        this.kaitsmisOskus = kaitsmisOskus;
        this.elo = arvutaAlgneElo(); // Algne ELO reiting
    }

        /**
         * Arvutab mängija algse ELO reitingu tema oskuste põhjal.
         * Kasutab kaalutud oskuste keskmist, et peegeldada erinevate
         * oskuste olulisust võrkpallis.
         */
        private int arvutaAlgneElo() {
            // Põhi-ELO, millele lisandub oskuste panus
            final int BAAS_ELO = 1000;

            // Oskuste kaalud võrkpalli kontekstis
            final double SERVIMISE_KAAL = 0.8;    // oluline punktide alustamiseks
            final double RÜNDAMISE_KAAL = 1.2;    // veidi olulisem punktide võitmiseks
            final double BLOKEERIMISE_KAAL = 0.8; // mitte nii tähtis
            final double KAITSMISE_KAAL = 1.2;    // väga tähtis

            // Kogukaal normaliseerimiseks
            final double KOGU_KAAL = SERVIMISE_KAAL + RÜNDAMISE_KAAL + BLOKEERIMISE_KAAL + KAITSMISE_KAAL;

            // Arvuta kaalutud keskmine oskuste tase (0-100)
            double kaalutudKeskmine = (servimisOskus * SERVIMISE_KAAL +
                    ründamisOskus * RÜNDAMISE_KAAL +
                    blokeerimisOskus * BLOKEERIMISE_KAAL +
                    kaitsmisOskus * KAITSMISE_KAAL) / KOGU_KAAL;

            // Arvuta ELO oskuste põhjal (10 ELO punkti iga oskuspunkti kohta)
            int oskusteElo = (int)Math.round(kaalutudKeskmine * 10);

            return BAAS_ELO + oskusteElo;
        }

    public String getNimi() {
        return nimi;
    }

    public int getElo() {
        return elo;
    }


    public void uuendaElo(int muutus) {
        this.elo += muutus;
    }


    public int getServimisOskus() {
        return servimisOskus;
    }


    public int getRündamisOskus() {
        return ründamisOskus;
    }


    public int getBlokeerimisOskus() {
        return blokeerimisOskus;
    }


    public int getKaitsmisOskus() {
        return kaitsmisOskus;
    }

    public String getNõrgimOskus() {
        int miinimum = Math.min(Math.min(servimisOskus, ründamisOskus),
                Math.min(blokeerimisOskus, kaitsmisOskus));

        if (miinimum == servimisOskus) return "servimine";
        if (miinimum == ründamisOskus) return "ründamine";
        if (miinimum == blokeerimisOskus) return "blokeerimine";
        return "kaitsmine";
    }


    @Override
    public String toString() {
        return "Mängija: " + nimi +
                "\nELO: " + elo +
                "\nServimisoskus: " + servimisOskus +
                "\nRündamisoskus: " + ründamisOskus +
                "\nBlokeerimisoskus: " + blokeerimisOskus +
                "\nKaitsmisoskus: " + kaitsmisOskus;
    }


    public void setServimisOskus(int servimisOskus) {
        this.servimisOskus = Math.min(100, Math.max(0, servimisOskus));
    }


    public void setRündamisOskus(int ründamisOskus) {
        this.ründamisOskus = Math.min(100, Math.max(0, ründamisOskus));
    }


    public void setBlokeerimisOskus(int blokeerimisOskus) {
        this.blokeerimisOskus = Math.min(100, Math.max(0, blokeerimisOskus));
    }


    public void setKaitsmisOskus(int kaitsmisOskus) {
        this.kaitsmisOskus = Math.min(100, Math.max(0, kaitsmisOskus));
    }
}