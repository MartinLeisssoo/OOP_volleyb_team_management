import javax.swing.JOptionPane;

/**
 * Peaklass, mis käivitab treeneri rakenduse ja haldab kasutaja suhtlust.
 */
public class TreenerRakendus {
    private Võistkond[] võistkonnad;  // Kõik võistkonnad
    private Võistkond aktiivneVõistkond;  // Praegu valitud võistkond
    private int võistkondeArv;  // Võistkondade arv
    private final int MAKS_VÕISTKONDI = 10;  // Maksimaalne võistkondade arv

    private TreeninguSimulaator simulaator;
    private TreeninguSoovitaja soovitaja;

    public TreenerRakendus() {
        this.võistkonnad = new Võistkond[MAKS_VÕISTKONDI];
        this.võistkondeArv = 0;
        this.simulaator = new TreeninguSimulaator();
        this.soovitaja = new TreeninguSoovitaja();

        // Loo vaikimisi võistkond
        Võistkond vaikimisiVõistkond = new Võistkond("Minu Võistkond", 20);
        lisaVõistkond(vaikimisiVõistkond);
        aktiivneVõistkond = vaikimisiVõistkond;
    }

    /**
     * Lisab uue võistkonna
     */
    public boolean lisaVõistkond(Võistkond võistkond) {
        if (võistkondeArv < MAKS_VÕISTKONDI) {
            võistkonnad[võistkondeArv] = võistkond;
            võistkondeArv++;
            return true;
        }
        return false;
    }

    /**
     * Käivitab rakenduse.
     */
    public void käivita() {
        JOptionPane.showMessageDialog(null,
                "Tere tulemast Võrkpalli Treeneri rakendusse!\n\n" +
                        "See rakendus aitab sul hallata oma võistkonda,\n" +
                        "simuleerida treeninguid ja saada soovitusi mängijate arendamiseks.",
                "Võrkpalli Treener", JOptionPane.INFORMATION_MESSAGE);

        boolean töötab = true;

        while (töötab) {
            String valik = JOptionPane.showInputDialog(null,
                    "Vali tegevus:\n" +
                            "1 - Lisa mängija\n" +
                            "2 - Simuleeri treeningut\n" +
                            "3 - Vaata treeningusoovitusi\n" +
                            "4 - Näita kõiki mängijaid\n" +
                            "5 - Lisa uus võistkond\n" +
                            "6 - Vali aktiivne võistkond\n" +
                            "7 - Näita kõiki võistkondi\n" +
                            "8 - Välju",
                    "Võrkpalli Treener (" + aktiivneVõistkond.getNimi() + ")", JOptionPane.QUESTION_MESSAGE);

            if (valik == null) {
                töötab = false;
                continue;
            }

            switch (valik) {
                case "1":
                    lisaMängija();
                    break;
                case "2":
                    simuleeriTreening();
                    break;
                case "3":
                    vaataTreeningusoovitusi();
                    break;
                case "4":
                    näitaKõikiMängijaid();
                    break;
                case "5":
                    lisaUusVõistkond();
                    break;
                case "6":
                    valiAktiivneVõistkond();
                    break;
                case "7":
                    näitaKõikiVõistkondi();
                    break;
                case "8":
                    töötab = false;
                    JOptionPane.showMessageDialog(null, "Täname kasutamast! Head treenimist!",
                            "Võrkpalli Treener", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Palun vali kehtiv tegevus!",
                            "Viga", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Lisab uue võistkonna süsteemi.
     */
    private void lisaUusVõistkond() {
        String nimi = JOptionPane.showInputDialog(null, "Sisesta võistkonna nimi:",
                "Võistkonna lisamine", JOptionPane.QUESTION_MESSAGE);

        if (nimi == null || nimi.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Võistkonna lisamine tühistati!",
                    "Tühistatud", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int maksimumMängijad;
        try {
            maksimumMängijad = Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Sisesta maksimaalne mängijate arv:", "Võistkonna lisamine", JOptionPane.QUESTION_MESSAGE));

            if (maksimumMängijad <= 0) {
                JOptionPane.showMessageDialog(null, "Mängijate arv peab olema positiivne number!",
                        "Viga", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Palun sisesta kehtiv number!",
                    "Viga", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Võistkond uusVõistkond = new Võistkond(nimi, maksimumMängijad);

        if (lisaVõistkond(uusVõistkond)) {
            JOptionPane.showMessageDialog(null, "Võistkond '" + nimi + "' lisati edukalt!",
                    "Võistkond lisatud", JOptionPane.INFORMATION_MESSAGE);

            // Küsi, kas soovib muuta aktiivset võistkonda
            int vastus = JOptionPane.showConfirmDialog(null,
                    "Kas soovid uue võistkonna '" + nimi + "' muuta aktiivseks?",
                    "Aktiivse võistkonna muutmine", JOptionPane.YES_NO_OPTION);

            if (vastus == JOptionPane.YES_OPTION) {
                aktiivneVõistkond = uusVõistkond;
                JOptionPane.showMessageDialog(null, "Aktiivne võistkond on nüüd '" + nimi + "'",
                        "Aktiivne võistkond", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Võistkondade arv on maksimaalse piiri peal (" + MAKS_VÕISTKONDI + ")!",
                    "Viga", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valib aktiivse võistkonna.
     */
    private void valiAktiivneVõistkond() {
        if (võistkondeArv <= 1) {
            JOptionPane.showMessageDialog(null, "Sul on ainult üks võistkond, pole midagi valida!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Koosta võistkondade nimekiri
        String[] nimed = new String[võistkondeArv];
        for (int i = 0; i < võistkondeArv; i++) {
            nimed[i] = võistkonnad[i].getNimi();
        }

        // Lase kasutajal valida võistkond
        String valitudNimi = (String) JOptionPane.showInputDialog(null,
                "Vali aktiivne võistkond:", "Võistkonna valimine",
                JOptionPane.QUESTION_MESSAGE, null, nimed, aktiivneVõistkond.getNimi());

        if (valitudNimi == null) {
            return;
        }

        // Leia valitud võistkond ja muuda see aktiivseks
        for (int i = 0; i < võistkondeArv; i++) {
            if (võistkonnad[i].getNimi().equals(valitudNimi)) {
                aktiivneVõistkond = võistkonnad[i];
                JOptionPane.showMessageDialog(null, "Aktiivne võistkond on nüüd '" + valitudNimi + "'",
                        "Aktiivne võistkond", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    /**
     * Näitab kõiki süsteemis olevaid võistkondi.
     */
    private void näitaKõikiVõistkondi() {
        StringBuilder info = new StringBuilder();
        info.append("Võistkonnad süsteemis (" + võistkondeArv + "):\n\n");

        for (int i = 0; i < võistkondeArv; i++) {
            Võistkond v = võistkonnad[i];
            info.append((v == aktiivneVõistkond ? "* " : "  "));
            info.append(v.getNimi()).append(" (");
            info.append(v.getMängijateArv()).append("/");
            info.append(v.getKõikMängijad().length).append(" mängijat)\n");
        }

        info.append("\n* - aktiivne võistkond");

        JOptionPane.showMessageDialog(null, info.toString(),
                "Kõik võistkonnad", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Lisab uue mängija aktiivsesse võistkonda.
     */
    private void lisaMängija() {
        String nimi = JOptionPane.showInputDialog(null, "Sisesta mängija nimi:",
                "Mängija lisamine", JOptionPane.QUESTION_MESSAGE);

        if (nimi == null || nimi.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mängija lisamine tühistati!",
                    "Tühistatud", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int servimisOskus = Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Sisesta servimisoskus (0-100):", "Mängija lisamine", JOptionPane.QUESTION_MESSAGE));

            int ründamisOskus = Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Sisesta ründamisoskus (0-100):", "Mängija lisamine", JOptionPane.QUESTION_MESSAGE));

            int blokeerimisOskus = Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Sisesta blokeerimisoskus (0-100):", "Mängija lisamine", JOptionPane.QUESTION_MESSAGE));

            int kaitsmisOskus = Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Sisesta kaitsmisoskus (0-100):", "Mängija lisamine", JOptionPane.QUESTION_MESSAGE));

            // Kontrolli, et oskused oleksid vahemikus 0-100
            if (servimisOskus < 0 || servimisOskus > 100 ||
                    ründamisOskus < 0 || ründamisOskus > 100 ||
                    blokeerimisOskus < 0 || blokeerimisOskus > 100 ||
                    kaitsmisOskus < 0 || kaitsmisOskus > 100) {

                JOptionPane.showMessageDialog(null, "Kõik oskused peavad olema vahemikus 0-100!",
                        "Viga", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Mängija uusMängija = new Mängija(nimi, servimisOskus, ründamisOskus, blokeerimisOskus, kaitsmisOskus);

            if (aktiivneVõistkond.lisaMängija(uusMängija)) {
                JOptionPane.showMessageDialog(null, "Mängija " + nimi + " lisati edukalt võistkonda '" +
                                aktiivneVõistkond.getNimi() + "'!",
                        "Mängija lisatud", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Võistkond on täis, ei saa rohkem mängijaid lisada!",
                        "Viga", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Palun sisesta kehtivad numbrid oskuste jaoks!",
                    "Viga", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Simuleerib treeningut valitud mängijale aktiivses võistkonnas.
     */
    private void simuleeriTreening() {
        if (aktiivneVõistkond.getMängijateArv() == 0) {
            JOptionPane.showMessageDialog(null, "Võistkonnas pole ühtegi mängijat! Lisa esmalt mängijad.",
                    "Viga", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Koosta mängijate nimekiri
        Mängija[] mängijad = aktiivneVõistkond.getKõikMängijad();
        String[] nimed = new String[mängijad.length];
        for (int i = 0; i < mängijad.length; i++) {
            nimed[i] = mängijad[i].getNimi();
        }

        // Lase kasutajal valida mängija
        String valitudNimi = (String) JOptionPane.showInputDialog(null,
                "Vali mängija treeningu simuleerimiseks:", "Treeningu simuleerimine",
                JOptionPane.QUESTION_MESSAGE, null, nimed, nimed[0]);

        if (valitudNimi == null) {
            return;
        }

        Mängija valitudMängija = aktiivneVõistkond.leiaMängijaNimeJärgi(valitudNimi);

        // Salvesta oskuste algsed väärtused
        int algneServimine = valitudMängija.getServimisOskus();
        int algneRündamine = valitudMängija.getRündamisOskus();
        int algneBlokeerimine = valitudMängija.getBlokeerimisOskus();
        int algneKaitsmine = valitudMängija.getKaitsmisOskus();

        // Simuleeri treening
        TreeninguTulemus tulemus = simulaator.simuleeriTreening(valitudMängija);
        int eloMuutus = simulaator.arvutaEloMuutus(valitudMängija, tulemus);

        // Uuenda mängija ELO reitingut ja oskusi
        valitudMängija.uuendaElo(eloMuutus);
        int[] oskusteParanemine = simulaator.uuendaOskusi(valitudMängija, tulemus);

        // Näita tulemusi
        StringBuilder tulemused = new StringBuilder();
        tulemused.append("--- Treeningu tulemused mängijale ").append(valitudMängija.getNimi()).append(" ---\n");
        tulemused.append("Servimise tulemus: ").append(String.format("%.1f", tulemus.getServimiseÕnnestumismäär() * 100)).append("%\n");
        tulemused.append("Ründamise tulemus: ").append(String.format("%.1f", tulemus.getRündamiseÕnnestumismäär() * 100)).append("%\n");
        tulemused.append("Blokeerimise tulemus: ").append(String.format("%.1f", tulemus.getBlokeerimiseÕnnestumismäär() * 100)).append("%\n");
        tulemused.append("Kaitsmise tulemus: ").append(String.format("%.1f", tulemus.getKaitsmiseÕnnestumismäär() * 100)).append("%\n\n");
        tulemused.append("Nõrgim oskus treeningul: ").append(tulemus.getNõrgimOskus()).append("\n\n");

        // Lisa oskuste parandamise info
        tulemused.append("--- Oskuste muutused ---\n");
        tulemused.append("Servimine: ").append(algneServimine);
        if (oskusteParanemine[0] > 0) {
            tulemused.append(" → ").append(valitudMängija.getServimisOskus()).append(" (+").append(oskusteParanemine[0]).append(")\n");
        } else {
            tulemused.append(" (muutuseta)\n");
        }

        tulemused.append("Ründamine: ").append(algneRündamine);
        if (oskusteParanemine[1] > 0) {
            tulemused.append(" → ").append(valitudMängija.getRündamisOskus()).append(" (+").append(oskusteParanemine[1]).append(")\n");
        } else {
            tulemused.append(" (muutuseta)\n");
        }

        tulemused.append("Blokeerimine: ").append(algneBlokeerimine);
        if (oskusteParanemine[2] > 0) {
            tulemused.append(" → ").append(valitudMängija.getBlokeerimisOskus()).append(" (+").append(oskusteParanemine[2]).append(")\n");
        } else {
            tulemused.append(" (muutuseta)\n");
        }

        tulemused.append("Kaitsmine: ").append(algneKaitsmine);
        if (oskusteParanemine[3] > 0) {
            tulemused.append(" → ").append(valitudMängija.getKaitsmisOskus()).append(" (+").append(oskusteParanemine[3]).append(")\n");
        } else {
            tulemused.append(" (muutuseta)\n");
        }

        tulemused.append("\nELO muutus: ").append(eloMuutus >= 0 ? "+" : "").append(eloMuutus).append("\n");
        tulemused.append("Uus ELO: ").append(valitudMängija.getElo());

        JOptionPane.showMessageDialog(null, tulemused.toString(),
                "Treeningu tulemused", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Näitab kõiki aktiivse võistkonna mängijaid.
     */
    private void näitaKõikiMängijaid() {
        if (aktiivneVõistkond.getMängijateArv() == 0) {
            JOptionPane.showMessageDialog(null, "Võistkonnas pole ühtegi mängijat!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Mängija[] mängijad = aktiivneVõistkond.getKõikMängijad();
        StringBuilder info = new StringBuilder();
        info.append("Võistkond: ").append(aktiivneVõistkond.getNimi()).append("\n");
        info.append("Mängijate arv: ").append(aktiivneVõistkond.getMängijateArv()).append("\n\n");

        for (Mängija m : mängijad) {
            info.append("Nimi: ").append(m.getNimi()).append("\n");
            info.append("ELO: ").append(m.getElo()).append("\n");
            info.append("Servimisoskus: ").append(m.getServimisOskus()).append("\n");
            info.append("Ründamisoskus: ").append(m.getRündamisOskus()).append("\n");
            info.append("Blokeerimisoskus: ").append(m.getBlokeerimisOskus()).append("\n");
            info.append("Kaitsmisoskus: ").append(m.getKaitsmisOskus()).append("\n");
            info.append("Nõrgim oskus: ").append(m.getNõrgimOskus()).append("\n\n");
        }

        JOptionPane.showMessageDialog(null, info.toString(),
                "Võistkonna mängijad", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Rakenduse käivituspunkt.
     *
     * @param args Käsurea argumendid (ei kasutata)
     */
    public static void main(String[] args) {
        TreenerRakendus rakendus = new TreenerRakendus();

        Võistkond tiigrid = new Võistkond("Tartu Tiigrid", 15);
        Võistkond pääsukesed = new Võistkond("Pärnu Pääsukesed", 12);

        // Lisa võistkonnad rakendusse
        rakendus.lisaVõistkond(tiigrid);
        rakendus.lisaVõistkond(pääsukesed);


        // Loon mängijad

        Mängija kask = new Mängija("Kalle Kask", 85, 90, 75, 88); // Kogenud, hea rünnak/kaitse
        Mängija tamm = new Mängija("Tõnu Tamm", 70, 95, 65, 72); // Noor ründaja
        Mängija kuusk = new Mängija("Kertu Kuusk", 60, 70, 92, 95); // Kaitse/bloki spetsialist
        Mängija paju = new Mängija("Pille Paju", 75, 78, 72, 80); // Tasakaalus


        Mängija lepp = new Mängija("Lauri Lepp", 80, 82, 85, 83); // Hea universaal
        Mängija saar = new Mängija("Siim Saar", 96, 70, 68, 75); // Servi ekspert
        Mängija vaher = new Mängija("Vello Vaher", 55, 60, 50, 62); // Algaja
        Mängija kallas = new Mängija("Kristi Kallas", 78, 75, 88, 85); // Hea blokk/kaitse


        tiigrid.lisaMängija(kask);
        tiigrid.lisaMängija(tamm);
        tiigrid.lisaMängija(kuusk);
        tiigrid.lisaMängija(paju);

        pääsukesed.lisaMängija(lepp);
        pääsukesed.lisaMängija(saar);
        pääsukesed.lisaMängija(vaher);
        pääsukesed.lisaMängija(kallas);

        rakendus.käivita();
    }

    /**
     * Näitab treeningusoovitusi valitud mängijale aktiivses võistkonnas.
     */
    private void vaataTreeningusoovitusi() {
        if (aktiivneVõistkond.getMängijateArv() == 0) {
            JOptionPane.showMessageDialog(null, "Võistkonnas pole ühtegi mängijat! Lisa esmalt mängijad.",
                    "Viga", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Koosta mängijate nimekiri
        Mängija[] mängijad = aktiivneVõistkond.getKõikMängijad();
        String[] nimed = new String[mängijad.length];
        for (int i = 0; i < mängijad.length; i++) {
            nimed[i] = mängijad[i].getNimi();
        }

        // Lase kasutajal valida mängija
        String valitudNimi = (String) JOptionPane.showInputDialog(null,
                "Vali mängija soovituste vaatamiseks:", "Treeningusoovitused",
                JOptionPane.QUESTION_MESSAGE, null, nimed, nimed[0]);

        if (valitudNimi == null) {
            return;
        }

        Mängija valitudMängija = aktiivneVõistkond.leiaMängijaNimeJärgi(valitudNimi);

        // Simuleeri treening, et saada soovitusi
        TreeninguTulemus tulemus = simulaator.simuleeriTreening(valitudMängija);
        String soovitus = soovitaja.genereeriSoovitus(valitudMängija, tulemus);

        JOptionPane.showMessageDialog(null, soovitus,
                "Treeningusoovitused: " + valitudMängija.getNimi(),
                JOptionPane.INFORMATION_MESSAGE);
    }
}