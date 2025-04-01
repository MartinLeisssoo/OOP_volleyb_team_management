/**
 * Klass genereerib treeningusoovitusi treeningu tulemuste põhjal.
 */
public class TreeninguSoovitaja {
    /**
     * Genereerib treeningusoovituse mängijale põhinedes tema üldistel oskustel.
     * -Mängija, kellele soovitust genereeritakse
     * -Treeningu tulemus (säilitatud ühilduvuse jaoks, ei kasutata)
     * tagastab Treeningusoovituse
     */
    public String genereeriSoovitus(Mängija mängija, TreeninguTulemus tulemus) {
        // Leia nõrgim oskus mängija oskuste põhjal
        String nõrgimOskus = mängija.getNõrgimOskus();
        int oskusTase = 0;

        // Määra oskustase vastavalt nõrgimale oskusele
        switch (nõrgimOskus) {
            case "servimine":
                oskusTase = mängija.getServimisOskus();
                break;
            case "ründamine":
                oskusTase = mängija.getRündamisOskus();
                break;
            case "blokeerimine":
                oskusTase = mängija.getBlokeerimisOskus();
                break;
            case "kaitsmine":
                oskusTase = mängija.getKaitsmisOskus();
                break;
        }

        // Koosta soovitus
        StringBuilder soovitus = new StringBuilder();
        soovitus.append("Mängija ").append(mängija.getNimi())
                .append(" peaks keskenduma oskusele: ").append(nõrgimOskus).append("\n");
        soovitus.append("Praegune tase: ").append(oskusTase).append("/100\n\n");

        // Lihtsamad soovitused vastavalt oskusele ja tasemele
        boolean algaja = oskusTase < 50;

        if (nõrgimOskus.equals("servimine")) {
            if (algaja) {
                soovitus.append("- Harjutada korrektset põhihoiakut ja pallitabamist\n");
                soovitus.append("- Treenida täpsust, kasutades väljakul sihtmärke\n");
            } else {
                soovitus.append("- Arendada erinevaid servitüüpe ja täpsust\n");
                soovitus.append("- Harjutada servi pingelistes olukordades\n");
            }
        } else if (nõrgimOskus.equals("ründamine")) {
            if (algaja) {
                soovitus.append("- Harjutada hüppetehnikat ja ajastust\n");
                soovitus.append("- Arendada käe ja õla koordinatsiooni\n");
            } else {
                soovitus.append("- Harjutada erinevaid löögitüüpe ja suunamist\n");
                soovitus.append("- Arendada kohanemisvõimet erinevate tõstetega\n");
            }
        } else if (nõrgimOskus.equals("blokeerimine")) {
            if (algaja) {
                soovitus.append("- Harjutada käte asendit ja ajastust\n");
                soovitus.append("- Arendada hüppevõimet ja positsioneerimist\n");
            } else {
                soovitus.append("- Treenida koostööd teiste blokeerijatega\n");
                soovitus.append("- Arendada ründajate lugemist ja ennetamist\n");
            }
        } else if (nõrgimOskus.equals("kaitsmine")) {
            if (algaja) {
                soovitus.append("- Harjutada kaitseasendeid ja liikumisi\n");
                soovitus.append("- Arendada reageerimiskiirust\n");
            } else {
                soovitus.append("- Treenida kaitsetööd erinevate rünnakutüüpide vastu\n");
                soovitus.append("- Arendada koostööd blokeerijate ja teiste kaitsemängijatega\n");
            }
        }

        return soovitus.toString();
    }

    /**
     * Kuvab treeningu tulemused ja soovitused
     * mängija Mängija
     * tulemus Treeningu tulemused
     * eloMuutus ELO muutus pärast treeningut
     */
    public void kuvaTreeninguplaan(Mängija mängija, TreeninguTulemus tulemus, int eloMuutus) {
        String soovitus = genereeriSoovitus(mängija, tulemus);

        System.out.println("\n--- Treeningu tulemused mängijale " + mängija.getNimi() + " ---");
        System.out.println(tulemus);
        System.out.println("\nNõrgim oskus: " + tulemus.getNõrgimOskus());
        System.out.println("\n" + soovitus);
        System.out.println("\nELO muutus: " + (eloMuutus >= 0 ? "+" : "") + eloMuutus);
        System.out.println("Uus ELO: " + mängija.getElo());
    }
}