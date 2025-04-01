/**
 * Klass esindab treeningu tulemusi iga oskuse jaoks.
 */
public class TreeninguTulemus {
    private int servimisÕnnestumised;
    private int servimisKatsed;
    private int ründamiseÕnnestumised;
    private int ründamiseKatsed;
    private int blokeerimiseÕnnestumised;
    private int blokeerimiseKatsed;
    private int kaitsmiseÕnnestumised;
    private int kaitsmiseKatsed;


    public TreeninguTulemus() {
        this.servimisÕnnestumised = 0;
        this.servimisKatsed = 0;
        this.ründamiseÕnnestumised = 0;
        this.ründamiseKatsed = 0;
        this.blokeerimiseÕnnestumised = 0;
        this.blokeerimiseKatsed = 0;
        this.kaitsmiseÕnnestumised = 0;
        this.kaitsmiseKatsed = 0;
    }

    /**
     * Lisab servimise tulemuse.
     *
     * @param õnnestumine true, kui servimine õnnestus, false vastasel juhul
     */
    public void lisaServimiseTulemus(boolean õnnestumine) {
        servimisKatsed++;
        if (õnnestumine) {
            servimisÕnnestumised++;
        }
    }

    public void lisaRündamiseTulemus(boolean õnnestumine) {
        ründamiseKatsed++;
        if (õnnestumine) {
            ründamiseÕnnestumised++;
        }
    }


    public void lisaBlokeerimiseTulemus(boolean õnnestumine) {
        blokeerimiseKatsed++;
        if (õnnestumine) {
            blokeerimiseÕnnestumised++;
        }
    }


    public void lisaKaitsmiseTulemus(boolean õnnestumine) {
        kaitsmiseKatsed++;
        if (õnnestumine) {
            kaitsmiseÕnnestumised++;
        }
    }


    public double getÕnnestumismäär() {
        int koguÕnnestumised = servimisÕnnestumised + ründamiseÕnnestumised +
                blokeerimiseÕnnestumised + kaitsmiseÕnnestumised;
        int koguKatsed = servimisKatsed + ründamiseKatsed + blokeerimiseKatsed + kaitsmiseKatsed;

        if (koguKatsed == 0) return 0.0;
        return (double) koguÕnnestumised / koguKatsed;
    }

    /**
     * Leiab nõrgima oskuse õnnestumismäära põhjal
     * Nõrgima oskuse nimi
     */
    public String getNõrgimOskus() {
        double servimismäär = servimisKatsed > 0 ? (double) servimisÕnnestumised / servimisKatsed : 1.0;
        double ründamismäär = ründamiseKatsed > 0 ? (double) ründamiseÕnnestumised / ründamiseKatsed : 1.0;
        double blokeerimismäär = blokeerimiseKatsed > 0 ? (double) blokeerimiseÕnnestumised / blokeerimiseKatsed : 1.0;
        double kaitsmismäär = kaitsmiseKatsed > 0 ? (double) kaitsmiseÕnnestumised / kaitsmiseKatsed : 1.0;

        double miinimum = Math.min(Math.min(servimismäär, ründamismäär),
                Math.min(blokeerimismäär, kaitsmismäär));

        if (miinimum == servimismäär) return "servimine";
        if (miinimum == ründamismäär) return "ründamine";
        if (miinimum == blokeerimismäär) return "blokeerimine";
        return "kaitsmine";
    }

    /**
     * Tagastab servimise õnnestumismäära.
     */
    public double getServimiseÕnnestumismäär() {
        if (servimisKatsed == 0) return 0.0;
        return (double) servimisÕnnestumised / servimisKatsed;
    }

    /**
     * Tagastab ründamise õnnestumismäära.
     */
    public double getRündamiseÕnnestumismäär() {
        if (ründamiseKatsed == 0) return 0.0;
        return (double) ründamiseÕnnestumised / ründamiseKatsed;
    }

    /**
     * Tagastab blokeerimise õnnestumismäära.
     */
    public double getBlokeerimiseÕnnestumismäär() {
        if (blokeerimiseKatsed == 0) return 0.0;
        return (double) blokeerimiseÕnnestumised / blokeerimiseKatsed;
    }

    /**
     * Tagastab kaitsmise õnnestumismäära.
     */
    public double getKaitsmiseÕnnestumismäär() {
        if (kaitsmiseKatsed == 0) return 0.0;
        return (double) kaitsmiseÕnnestumised / kaitsmiseKatsed;
    }

    /**
     * Väljastab treeningu tulemused sõnena.
     */
    @Override
    public String toString() {
        return "Servimise tulemus: " + servimisÕnnestumised + "/" + servimisKatsed +
                "\nRündamise tulemus: " + ründamiseÕnnestumised + "/" + ründamiseKatsed +
                "\nBlokeerimise tulemus: " + blokeerimiseÕnnestumised + "/" + blokeerimiseKatsed +
                "\nKaitsmise tulemus: " + kaitsmiseÕnnestumised + "/" + kaitsmiseKatsed +
                "\nÜldine õnnestumismäär: " + String.format("%.1f", getÕnnestumismäär() * 100) + "%";
    }
}