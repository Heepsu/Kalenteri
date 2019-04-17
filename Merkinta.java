/*
 * Rajapinta erilaisten kalenterimerkintöjen tekemiseksi
 * Kaikkien merkintöjen pitää toteuttaa ainakin metodit lisäämistä, poistamista ja merkinnän tietokannasta hakemista varten
 */

interface Merkinta {
	void lisaa();
	void poista(int id);
	void muokkaa(int id);
}