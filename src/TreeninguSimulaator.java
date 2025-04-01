import java.util.Random;

/**
 * Klass simuleerib treeningu tulemusi kasutades juhuslikke arve.
 * Sisaldab loogikat tegevuste õnnestumise simuleerimiseks, ELO muutuse
 * arvutamiseks ja oskuste uuendamiseks treeningu põhjal.
 */
public class TreeninguSimulaator {
    private Random juhuslik;
    private final int KATSETE_ARV = 10; // Fikseeritud katsete arv iga oskuse jaoks


    public TreeninguSimulaator() {
        this.juhuslik = new Random();
    }

    /**
     * Simuleerib üksikut tegevust (servimine, ründamine jne) oskustaseme põhjal.
     * Kasutab mittelineaarset kõverat, et vähendada väga kõrgete õnnestumismäärade
     * tõenäosust isegi kõrgete oskustasemete puhul.
     */
    public boolean simuleeriTegevus(int oskusTase) {
        // Arvuta tegelik õnnestumise tõenäosus protsentides, kasutades kõverat,
        // mis läheneb maksimumile aeglasemalt.
        // See valem piirab maksimaalse tegeliku edu tõenäosuse umbes 90%-ni, isegi kui oskus = 100.
        // Konstandid (97.0 ja 50.0) on häälestamiseks, et muuta raskusastet.
        double tegelikEduTõenäosusProtsent = 97.0 * (1.0 - Math.exp(- (double)oskusTase / 50.0));

        // Genereeri juhuslik arv 0-99
        int juhuslikArv = juhuslik.nextInt(100);

        // Võrdle juhuslikku arvu arvutatud tõenäosusega
        return juhuslikArv < tegelikEduTõenäosusProtsent;
    }

    /**
     * Simuleerib mängija treeningu tulemusi kõigi oskuste jaoks.
     * Kutsub iga oskuse jaoks kindla arvu kordi 'simuleeriTegevus' meetodit.
     */
    public TreeninguTulemus simuleeriTreening(Mängija mängija) {
        TreeninguTulemus tulemus = new TreeninguTulemus();

        // Simuleeri servimist
        for (int i = 0; i < KATSETE_ARV; i++) {
            boolean õnnestumine = simuleeriTegevus(mängija.getServimisOskus());
            tulemus.lisaServimiseTulemus(õnnestumine);
        }

        // Simuleeri ründamist
        for (int i = 0; i < KATSETE_ARV; i++) {
            boolean õnnestumine = simuleeriTegevus(mängija.getRündamisOskus());
            tulemus.lisaRündamiseTulemus(õnnestumine);
        }

        // Simuleeri blokeerimist
        for (int i = 0; i < KATSETE_ARV; i++) {
            boolean õnnestumine = simuleeriTegevus(mängija.getBlokeerimisOskus());
            tulemus.lisaBlokeerimiseTulemus(õnnestumine);
        }

        // Simuleeri kaitsmist
        for (int i = 0; i < KATSETE_ARV; i++) {
            boolean õnnestumine = simuleeriTegevus(mängija.getKaitsmisOskus());
            tulemus.lisaKaitsmiseTulemus(õnnestumine);
        }

        return tulemus;
    }

    /**
     * Arvutab oodatava soorituse määra põhinedes oskustasemel ELO võrdluseks.
     * Seda kasutatakse ELO muutuse arvutamisel, et võrrelda tegelikku sooritust
     * mängija oskustasemest tuleneva ootusega.
     */
    private double arvutaOodatavSooritus(double oskusTase) {
        // Kasutame valemit, mis sarnaneb tegevuse simuleerimise valemiga,
        // kuid võib olla erinevate konstantidega, et ELO muutus oleks tasakaalus.
        return 0.95 * (1 - Math.exp(-oskusTase / 45.0));
    }

    /**
     * Arvutab ELO muutuse vastavalt treeningu tulemustele, võrreldes tegelikku
     * sooritust oodatava sooritusega ja rakendades algajate kaitset.
     */
    public int arvutaEloMuutus(Mängija mängija, TreeninguTulemus tulemus) {
        final int K = 28;
        // Leevendusfaktor negatiivsetele muutustele (kui sooritus jääb alla ootuste)
        final double NEGATIIVSE_MUUTUSE_LEEVENDUS = 0.7;

        // Tegelik üldine õnnestumismäär treeningul
        double õnnestumismäär = tulemus.getÕnnestumismäär();

        // Arvuta mängija keskmine oskuste tase (0-100)
        double keskmineOskus = (mängija.getServimisOskus() +
                mängija.getRündamisOskus() +
                mängija.getBlokeerimisOskus() +
                mängija.getKaitsmisOskus()) / 4.0;

        // Algajate kaitsefaktor - mida madalam keskmine oskus, seda suurem kaitse (0 kuni 1)
        double algajateKaitse = Math.max(0, 60 - keskmineOskus) / 60.0;

        // Oodatav õnnestumismäär vastavalt mängija keskmisele oskusele
        double oodatavÕnnestumismäär = arvutaOodatavSooritus(keskmineOskus);

        // Võrdle tegelikku õnnestumismäära oodatavaga
        double suhtelineEdukus = õnnestumismäär - oodatavÕnnestumismäär;

        // Rakenda algajate kaitset ja negatiivse muutuse leevendust
        if (suhtelineEdukus < 0) {
            suhtelineEdukus *= (1.0 - algajateKaitse);
            suhtelineEdukus *= NEGATIIVSE_MUUTUSE_LEEVENDUS;
        }
        // Arvuta lõplik ELO muutus vähendatud K-faktoriga
        return (int) Math.round(K * suhtelineEdukus);
    }

    /**
     * Uuendab mängija oskusi treeningu tulemuse põhjal.
     * Kui teatud oskuse õnnestumismäär on üle dünaamilise lävendi, siis see oskus paraneb
     * Lävi on nüüd kõrgem ka algajatele ja tõuseb väga järsult kõrgete oskustasemete puhul
     * @return Massiiv oskuste muutustega [servimine, ründamine, blokeerimine, kaitsmine] (0 või 1 iga oskuse kohta)
     */
    public int[] uuendaOskusi(Mängija mängija, TreeninguTulemus tulemus) {
        // Oskuste muutused [servimine, ründamine, blokeerimine, kaitsmine]
        int[] muutused = new int[4];
        double oskusTase;
        double lävend; // Lävi, millest alates oskus paraneb

        // --- Servimine ---
        oskusTase = mängija.getServimisOskus();
        double servimiseÕnnestumismäär = tulemus.getServimiseÕnnestumismäär();
        lävend = 0.3 + 0.7 * Math.pow(oskusTase / 100.0, 5);
        if (servimiseÕnnestumismäär > lävend && oskusTase < 100) {
            mängija.setServimisOskus((int)oskusTase + 1); // Kasutame set'i, mis piirab 100-ga
            muutused[0] = 1;
        }

        // --- Ründamine ---
        oskusTase = mängija.getRündamisOskus();
        double ründamiseÕnnestumismäär = tulemus.getRündamiseÕnnestumismäär();
        lävend = 0.3 + 0.7 * Math.pow(oskusTase / 100.0, 5);
        if (ründamiseÕnnestumismäär > lävend && oskusTase < 100) {
            mängija.setRündamisOskus((int)oskusTase + 1);
            muutused[1] = 1;
        }

        // --- Blokeerimine ---
        oskusTase = mängija.getBlokeerimisOskus();
        double blokeerimiseÕnnestumismäär = tulemus.getBlokeerimiseÕnnestumismäär();
        lävend = 0.3 + 0.7 * Math.pow(oskusTase / 100.0, 5);
        if (blokeerimiseÕnnestumismäär > lävend && oskusTase < 100) {
            mängija.setBlokeerimisOskus((int)oskusTase + 1);
            muutused[2] = 1;
        }

        // --- Kaitsmine ---
        oskusTase = mängija.getKaitsmisOskus();
        double kaitsmiseÕnnestumismäär = tulemus.getKaitsmiseÕnnestumismäär();
        lävend = 0.3 + 0.7 * Math.pow(oskusTase / 100.0, 5);
        if (kaitsmiseÕnnestumismäär > lävend && oskusTase < 100) {
            mängija.setKaitsmisOskus((int)oskusTase + 1);
            muutused[3] = 1;
        }

        return muutused;
    }
}