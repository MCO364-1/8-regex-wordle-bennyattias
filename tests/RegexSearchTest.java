import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
class RegexSearchTest {
    RegexSearch regexSearch = new RegexSearch();
    @ParameterizedTest
    @ValueSource(strings = {"Bob", "Smith", "Joey"})
    void properName(String input) {
        boolean actual = regexSearch.properName(input);
        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"alen", "jake", "jACOB", "JOSH"})
    void improperName(String input) {
        boolean actual = regexSearch.properName(input);
        assertFalse(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12", "43.23", "-34.5", "+98.7", "0", "0.0230", "023"})
    void validInteger(String input) {
        //12, 43.23, -34.5, +98.7, 0, 0.0230 (but not 023)
        boolean actual = regexSearch.integer(input);
        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"023"})
    void invalidInteger(String input) {
        //12, 43.23, -34.5, +98.7, 0, 0.0230 (but not 023)
        boolean actual = regexSearch.integer(input);
        assertFalse(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"father", "mother", "grandfather", "grandmother", "great-great-grandfather"})
    void validAncestor(String input) {
        boolean actual = regexSearch.ancestor(input);
        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"great-mother", "great-father", "uncle", "sister"})
    void invalidAncestor(String input) {
        boolean actual = regexSearch.ancestor(input);
        assertFalse(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcdeedcba", "qwErttRewq", "EEEEEEEEEE"})
    void validPalindrom(String input) {
        boolean actual = regexSearch.palindrome(input);
        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"asdfgasdfg", "assadffdhh", "asdfggfds", "0987667890"})
    void invalidPalindrome(String input) {
        boolean actual = regexSearch.palindrome(input);
        assertFalse(actual);
    }


}