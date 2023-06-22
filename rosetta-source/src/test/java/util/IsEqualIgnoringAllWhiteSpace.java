package util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class IsEqualIgnoringAllWhiteSpace extends BaseMatcher<String> {

    public String expected;

    public static IsEqualIgnoringAllWhiteSpace ignoresAllWhitespaces(String expected) {
        return new IsEqualIgnoringAllWhiteSpace(expected);
    }

    public IsEqualIgnoringAllWhiteSpace(String expected) {
        this.expected = expected.replaceAll("[\\s\\n]", "");
    }

    @Override
    public boolean matches(Object actual) {
//        if (actual instanceof String) {
//            return expected.equals(((String)actual).replaceAll("[\\s\n]", ""));
//        } else {
//            return false;
//        }
        return expected.equals(actual);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(expected);
//        description.appendText(String.format("the given String should match '%s' without whitespaces", expected));
    }

}
